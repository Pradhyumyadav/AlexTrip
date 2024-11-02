package service;

import Model.Hotel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HotelAPI {

    private static final Logger logger = LoggerFactory.getLogger(HotelAPI.class);
    private final String googleApiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public HotelAPI() {
        Dotenv dotenv = Dotenv.configure()
                .directory("/Users/pradhyumyadav/IdeaProjects/AlexTripAgencyManagementSystem/src/main/java/service")
                .load();
        this.googleApiKey = Optional.ofNullable(dotenv.get("GOOGLE_API_KEY"))
                .orElseThrow(() -> new IllegalStateException("GOOGLE_API_KEY not set in .env file."));
    }

    public List<Hotel> fetchHotelsByCity(String cityName, int radius, String sortPrice, String duration, String activityType) {
        if (cityName == null || cityName.trim().isEmpty()) {
            logger.error("City name parameter is required.");
            throw new IllegalArgumentException("City name parameter is required.");
        }

        List<Hotel> hotels = new ArrayList<>();
        String urlString = String.format(
                "https://maps.googleapis.com/maps/api/place/textsearch/json?query=hotels+in+%s&radius=%d&key=%s",
                cityName, radius, googleApiKey
        );

        logger.info("Fetching hotels with URL: {}", urlString);

        try {
            JsonNode rootNode = fetchJsonFromUrl(urlString);
            JsonNode resultsNode = rootNode.path("results");
            logger.debug("API raw response: {}", rootNode);

            if (resultsNode.isMissingNode() || !resultsNode.isArray()) {
                logger.warn("No results found for the provided city.");
                return hotels;
            }

            for (JsonNode hotelNode : resultsNode) {
                Hotel hotel = parseHotel(hotelNode);
                if (matchesActivityType(hotel, activityType)) {
                    hotels.add(hotel);
                }
            }

            // Sort and filter the hotels based on parameters
            sortHotelsByPrice(hotels, sortPrice);
            hotels = filterByDuration(hotels, duration);

            logger.info("Number of hotels after filtering: {}", hotels.size());
        } catch (IOException e) {
            logger.error("Error while fetching hotels: ", e);
        }

        return hotels;
    }

    private boolean matchesActivityType(Hotel hotel, String activityType) {
        // Implement logic for filtering hotels by activity type if required
        return true; // Currently, all hotels are returned
    }

    private JsonNode fetchJsonFromUrl(String urlString) throws IOException {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                logger.error("Failed to fetch data from URL: {} - HTTP response code: {}", urlString, responseCode);
                throw new IOException("HTTP error code: " + responseCode);
            }

            try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8)) {
                String response = scanner.useDelimiter("\\A").next();
                return objectMapper.readTree(response);
            }

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private Hotel parseHotel(JsonNode hotelNode) {
        Hotel.Builder hotelBuilder = new Hotel.Builder();

        hotelBuilder.id(hotelNode.path("place_id").asText())
                .name(hotelNode.path("name").asText("Hotel Name Not Available"))
                .location(hotelNode.path("formatted_address").asText("Address Not Available"))
                .latitude(hotelNode.path("geometry").path("location").path("lat").asDouble())
                .longitude(hotelNode.path("geometry").path("location").path("lng").asDouble())
                .rating(hotelNode.path("rating").asDouble(0.0))
                .numberReviews(hotelNode.path("user_ratings_total").asInt(0))
                .activityType(getRandomActivityType())
                .stayDuration(new Random().nextInt(10) + 1); // Mock duration for filtering purposes

        // Set a mock price based on price level (if available)
        int priceLevel = hotelNode.path("price_level").asInt(2);
        hotelBuilder.price(new BigDecimal(50 * priceLevel));

        // Fetch photo URLs for the hotel
        List<String> photoUrls = parseHotelPhotos(hotelNode);
        hotelBuilder.imageUrls(photoUrls);

        return hotelBuilder.build();
    }

    private List<String> parseHotelPhotos(JsonNode hotelNode) {
        List<String> photoUrls = new ArrayList<>();
        if (hotelNode.has("photos")) {
            for (JsonNode photo : hotelNode.get("photos")) {
                String photoReference = photo.path("photo_reference").asText(null);
                if (photoReference != null && !photoReference.isEmpty()) {
                    String photoUrl = String.format(
                            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s",
                            photoReference, googleApiKey
                    );
                    photoUrls.add(photoUrl);
                }
            }
        } else {
            logger.warn("No photos found for hotel: {}", hotelNode.path("name").asText("Unnamed Hotel"));
        }
        return photoUrls;
    }

    private List<Hotel> filterByDuration(List<Hotel> hotels, String duration) {
        if (duration == null || duration.trim().isEmpty()) return hotels;

        return switch (duration.toLowerCase()) {
            case "short" -> hotels.stream().filter(hotel -> hotel.getStayDuration() <= 3).toList();
            case "medium" -> hotels.stream().filter(hotel -> hotel.getStayDuration() >= 4 && hotel.getStayDuration() <= 7).toList();
            case "long" -> hotels.stream().filter(hotel -> hotel.getStayDuration() >= 8).toList();
            default -> hotels; // Return unfiltered if duration is unrecognized
        };
    }

    private void sortHotelsByPrice(List<Hotel> hotels, String sortPrice) {
        if ("lowToHigh".equalsIgnoreCase(sortPrice)) {
            hotels.sort(Comparator.comparing(Hotel::getPrice));
        } else if ("highToLow".equalsIgnoreCase(sortPrice)) {
            hotels.sort(Comparator.comparing(Hotel::getPrice).reversed());
        }
    }

    private String getRandomActivityType() {
        String[] activityTypes = {"Leisure", "Business", "Family", "Adventure", "Romantic"};
        return activityTypes[new Random().nextInt(activityTypes.length)];
    }
}