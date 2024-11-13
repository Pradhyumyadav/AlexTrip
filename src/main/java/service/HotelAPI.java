package service;

import Model.Hotel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class HotelAPI {
    private static final Logger logger = LoggerFactory.getLogger(HotelAPI.class);
    private final String googleApiKey;
    private final String currencyApiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private BigDecimal cachedUsdToGbpRate = BigDecimal.ONE;

    public HotelAPI() {
        // Load the .env file explicitly from a specific directory
        Dotenv dotenv = Dotenv.configure()
                .directory("/Users/pradhyumyadav/IdeaProjects/AlexTripAgencyManagementSystem/src/main/java/service")
                .filename(".env")
                .load();

        this.googleApiKey = dotenv.get("GOOGLE_API_KEY");
        this.currencyApiKey = dotenv.get("FREECURRENCYAPI");

        if (googleApiKey == null || currencyApiKey == null) {
            throw new IllegalStateException("API keys for Google or Currency conversion are not set properly.");
        }
    }

    public Hotel fetchHotelDetails(String hotelId) throws IOException, InterruptedException {
        String url = String.format("https://maps.googleapis.com/maps/api/place/details/json?place_id=%s&key=%s", hotelId, googleApiKey);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            logger.error("Failed to fetch hotel details: HTTP error code {}", response.statusCode());
            return null;
        }

        JsonNode rootNode = objectMapper.readTree(response.body());
        JsonNode resultNode = rootNode.path("result");
        if (resultNode.isMissingNode()) {
            logger.error("No hotel details found for the given ID: {}", hotelId);
            return null;
        }

        BigDecimal usdToGbpRate = getUsdToGbpExchangeRate();
        return parseHotel(resultNode, usdToGbpRate);
    }

    public List<Hotel> fetchHotelsByCity(String city, int radius, String sortPrice, String duration, String activityType, String priceRange) throws IOException, InterruptedException {
        List<Hotel> hotels = new ArrayList<>();
        String urlString = String.format("https://maps.googleapis.com/maps/api/place/textsearch/json?query=hotels+in+%s&radius=%d&type=lodging&key=%s", city, radius, googleApiKey);

        JsonNode rootNode = fetchJsonFromUrl(urlString);
        JsonNode resultsNode = rootNode.path("results");

        if (resultsNode.isMissingNode() || !resultsNode.isArray()) {
            logger.warn("No results found for the specified city: {}", city);
            return hotels;
        }

        BigDecimal usdToGbpRate = getUsdToGbpExchangeRate();
        for (JsonNode hotelNode : resultsNode) {
            Hotel hotel = parseHotel(hotelNode, usdToGbpRate);
            if (hotel != null && matchesFilters(hotel, activityType, priceRange, duration)) {
                hotels.add(hotel);
            }
        }

        sortHotelsByPrice(hotels, sortPrice);
        return hotels;
    }

    private Hotel parseHotel(JsonNode hotelNode, BigDecimal usdToGbpRate) {
        String placeId = hotelNode.path("place_id").asText("");
        String name = hotelNode.path("name").asText("Name not available");
        String location = hotelNode.path("formatted_address").asText("Address not available");
        double latitude = hotelNode.path("geometry").path("location").path("lat").asDouble(0.0);
        double longitude = hotelNode.path("geometry").path("location").path("lng").asDouble(0.0);
        double rating = hotelNode.path("rating").asDouble(0.0);
        int numberReviews = hotelNode.path("user_ratings_total").asInt(0);
        BigDecimal price = new BigDecimal(hotelNode.path("price_level").asInt(20)); // Assuming $20 per level as placeholder

        BigDecimal priceInGBP = price.multiply(usdToGbpRate);

        List<String> imageUrls = new ArrayList<>();
        JsonNode photosNode = hotelNode.path("photos");
        if (photosNode.isArray()) {
            for (JsonNode photoNode : photosNode) {
                String photoReference = photoNode.path("photo_reference").asText();
                if (!photoReference.isEmpty()) {
                    String photoUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s", photoReference, googleApiKey);
                    imageUrls.add(photoUrl);
                }
            }
        }

        return new Hotel.Builder()
                .id(placeId)
                .placeId(placeId)
                .name(name)
                .location(location)
                .latitude(latitude)
                .longitude(longitude)
                .rating(rating)
                .numberReviews(numberReviews)
                .price(priceInGBP)
                .imageUrls(imageUrls)
                .description("Detailed description not available")
                .build();
    }

    private JsonNode fetchJsonFromUrl(String urlString) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readTree(response.body());
    }

    private void sortHotelsByPrice(List<Hotel> hotels, String sortPrice) {
        hotels.sort((h1, h2) -> "asc".equalsIgnoreCase(sortPrice) ? h1.getPrice().compareTo(h2.getPrice()) : h2.getPrice().compareTo(h1.getPrice()));
    }

    private boolean matchesFilters(Hotel hotel, String activityType, String priceRange, String duration) {
        // Here you could add filtering logic based on the activity type, price range, and duration
        // This is a placeholder for actual logic
        return true;
    }

    private BigDecimal getUsdToGbpExchangeRate() throws IOException, InterruptedException {
        if (cachedUsdToGbpRate.compareTo(BigDecimal.ONE) == 0) {  // Assumes default is unupdated
            updateExchangeRate();
        }
        return cachedUsdToGbpRate;
    }

    private void updateExchangeRate() throws IOException, InterruptedException {
        String url = String.format("https://api.freecurrencyapi.com/v1/latest?apikey=%s&base=USD&symbols=GBP", currencyApiKey);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonNode rootNode = objectMapper.readTree(response.body());
            cachedUsdToGbpRate = new BigDecimal(rootNode.path("data").path("GBP").asText("1"));  // Properly update the cached rate
        } else {
            logger.error("Failed to update exchange rate: HTTP error code {}", response.statusCode());
        }
    }
}