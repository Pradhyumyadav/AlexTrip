package service;

import Model.Hotel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class HotelAPI {

    private static final Logger logger = LoggerFactory.getLogger(HotelAPI.class);
    private final String googleApiKey;
    private final String currencyApiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient;

    // Cache variables for exchange rate and expiration time
    private BigDecimal cachedUsdToGbpRate = null;
    private Instant cacheExpirationTime = null;
    private static final Duration CACHE_DURATION = Duration.ofHours(24);

    public HotelAPI() {
        Dotenv dotenv = Dotenv.configure()
                .directory("/Users/pradhyumyadav/IdeaProjects/AlexTripAgencyManagementSystem/src/main/java/service")
                .load();
        this.googleApiKey = Optional.ofNullable(dotenv.get("GOOGLE_API_KEY"))
                .orElseThrow(() -> new IllegalStateException("GOOGLE_API_KEY not set in .env file."));
        this.currencyApiKey = Optional.ofNullable(dotenv.get("FREECURRENCYAPI"))
                .orElseThrow(() -> new IllegalStateException("FREECURRENCYAPI not set in .env file."));
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<Hotel> fetchHotelsByCity(String cityName, int radius, String sortPrice, String duration, String activityType, String priceRange) {
        List<Hotel> hotels = new ArrayList<>();
        String urlString = String.format(
                "https://maps.googleapis.com/maps/api/place/textsearch/json?query=hotels+in+%s&radius=%d&key=%s",
                cityName, radius, googleApiKey
        );

        try {
            JsonNode rootNode = fetchJsonFromUrl(urlString);
            logger.info("API Response: {}", rootNode.toString());
            JsonNode resultsNode = rootNode.path("results");

            if (resultsNode.isMissingNode() || !resultsNode.isArray()) {
                logger.warn("No results found for the specified city: {}", cityName);
                return hotels;
            }

            BigDecimal usdToGbpRate = getCachedUsdToGbpExchangeRate();

            for (JsonNode hotelNode : resultsNode) {
                Hotel hotel = parseHotel(hotelNode, usdToGbpRate);
                if (matchesFilters(hotel, activityType, priceRange)) {
                    hotels.add(hotel);
                }
            }

            sortHotelsByPrice(hotels, sortPrice);
            hotels = filterByDuration(hotels, duration);
        } catch (IOException | InterruptedException e) {
            logger.error("Error fetching hotels from API: ", e);
            Thread.currentThread().interrupt();
        }

        return hotels;
    }

    public Hotel fetchHotelDetails(String hotelId) {
        String urlString = String.format(
                "https://maps.googleapis.com/maps/api/place/details/json?place_id=%s&key=%s",
                hotelId, googleApiKey
        );

        try {
            JsonNode rootNode = fetchJsonFromUrl(urlString);
            JsonNode resultNode = rootNode.path("result");

            if (resultNode.isMissingNode()) {
                logger.warn("No details found for hotel ID: {}", hotelId);
                return null;
            }

            BigDecimal usdToGbpRate = getCachedUsdToGbpExchangeRate();
            return parseHotelDetails(resultNode, usdToGbpRate);
        } catch (IOException | InterruptedException e) {
            logger.error("Error fetching hotel details from API: ", e);
            Thread.currentThread().interrupt();
            return null;
        }
    }

    private BigDecimal getCachedUsdToGbpExchangeRate() throws IOException, InterruptedException {
        // Check if the cache is valid
        if (cachedUsdToGbpRate != null && cacheExpirationTime != null && Instant.now().isBefore(cacheExpirationTime)) {
            return cachedUsdToGbpRate;
        }

        // Fetch a new rate and update the cache
        String urlString = String.format("https://api.freecurrencyapi.com/v1/latest?apikey=%s&currencies=GBP", currencyApiKey);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonNode rootNode = objectMapper.readTree(response.body());
            JsonNode rateNode = rootNode.path("data").path("GBP");

            if (rateNode.isMissingNode()) {
                throw new IOException("GBP rate is missing in response.");
            }

            // Update the cache
            cachedUsdToGbpRate = rateNode.decimalValue();
            cacheExpirationTime = Instant.now().plus(CACHE_DURATION);
            return cachedUsdToGbpRate;
        } else {
            throw new IOException("Failed to fetch exchange rate from FreeCurrencyAPI. Status code: " + response.statusCode());
        }
    }

    private Hotel parseHotel(JsonNode hotelNode, BigDecimal usdToGbpRate) {
        return createHotelFromNode(hotelNode, usdToGbpRate);
    }

    private Hotel parseHotelDetails(JsonNode hotelNode, BigDecimal usdToGbpRate) {
        return createHotelFromNode(hotelNode, usdToGbpRate);
    }

    private Hotel createHotelFromNode(JsonNode hotelNode, BigDecimal usdToGbpRate) {
        BigDecimal priceInUsd = BigDecimal.valueOf(hotelNode.path("price_level").asInt(-1) * 50L);
        BigDecimal priceInGbp = priceInUsd.multiply(usdToGbpRate).setScale(2, RoundingMode.HALF_UP);

        List<String> imageUrls = new ArrayList<>();
        JsonNode photosNode = hotelNode.path("photos");

        if (photosNode.isArray()) {
            for (JsonNode photoNode : photosNode) {
                String photoReference = photoNode.path("photo_reference").asText();
                if (!photoReference.isEmpty()) {
                    String photoUrl = String.format(
                            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s",
                            photoReference, googleApiKey
                    );
                    imageUrls.add(photoUrl);
                }
            }
        }

        return new Hotel.Builder()
                .id(hotelNode.path("place_id").asText())
                .name(hotelNode.path("name").asText("Hotel Name Not Available"))
                .location(hotelNode.path("formatted_address").asText("Address Not Available"))
                .latitude(hotelNode.path("geometry").path("location").path("lat").asDouble())
                .longitude(hotelNode.path("geometry").path("location").path("lng").asDouble())
                .rating(hotelNode.path("rating").asDouble(0.0))
                .numberReviews(hotelNode.path("user_ratings_total").asInt(0))
                .price(priceInGbp)
                .imageUrls(imageUrls)
                .description(hotelNode.path("editorial_summary").path("overview").asText("Description Not Available"))
                .build();
    }

    private JsonNode fetchJsonFromUrl(String urlString) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readTree(response.body());
        } else {
            throw new IOException("Unexpected response code: " + response.statusCode());
        }
    }

    private boolean matchesFilters(Hotel hotel, String activityType, String priceRange) {
        return matchesActivityType(hotel, activityType) && matchesPriceRange(hotel, priceRange);
    }

    private boolean matchesActivityType(Hotel hotel, String activityType) {
        return activityType == null || activityType.isEmpty() || hotel.getActivityType().equalsIgnoreCase(activityType);
    }

    private boolean matchesPriceRange(Hotel hotel, String priceRange) {
        if (priceRange == null || priceRange.isEmpty()) return true;
        BigDecimal price = hotel.getPrice();
        return switch (priceRange.toLowerCase()) {
            case "low" -> price.compareTo(BigDecimal.valueOf(100)) <= 0;
            case "medium" -> price.compareTo(BigDecimal.valueOf(100)) > 0 && price.compareTo(BigDecimal.valueOf(500)) <= 0;
            case "high" -> price.compareTo(BigDecimal.valueOf(500)) > 0;
            default -> true;
        };
    }

    private void sortHotelsByPrice(List<Hotel> hotels, String sortPrice) {
        if ("lowToHigh".equalsIgnoreCase(sortPrice)) {
            hotels.sort(Comparator.comparing(Hotel::getPrice));
        } else if ("highToLow".equalsIgnoreCase(sortPrice)) {
            hotels.sort(Comparator.comparing(Hotel::getPrice).reversed());
        }
    }

    private List<Hotel> filterByDuration(List<Hotel> hotels, String duration) {
        if (duration == null || duration.isEmpty()) return hotels;
        return switch (duration.toLowerCase()) {
            case "short" -> hotels.stream().filter(hotel -> hotel.getStayDuration() <= 3).toList();
            case "medium" -> hotels.stream().filter(hotel -> hotel.getStayDuration() >= 4 && hotel.getStayDuration() <= 7).toList();
            case "long" -> hotels.stream().filter(hotel -> hotel.getStayDuration() >= 8).toList();
            default -> hotels;
        };
    }
}