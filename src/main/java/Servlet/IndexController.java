package Servlet;

import Model.Hotel;
import com.google.gson.*;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import service.HotelAPI;
import io.github.cdimascio.dotenv.Dotenv;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet({"/index", "/searchService"})
public class IndexController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(IndexController.class.getName());
    private final HotelAPI hotelAPI;
    private final Gson gson = new Gson();
    private final Dotenv dotenv;

    public IndexController() {
        this.hotelAPI = new HotelAPI();
        this.dotenv = Dotenv.configure()
                .directory("/Users/pradhyumyadav/IdeaProjects/AlexTripAgencyManagementSystem/src/main/java/service")
                .filename(".env")
                .load();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("GOOGLE_API_KEY", dotenv.get("GOOGLE_API_KEY"));

        String type = request.getParameter("type");  // Identify request type
        if ("buttonSearch".equals(type)) {
            handleButtonSearch(request, response);
        } else {
            handleLocationBasedSearch(request, response);
        }
    }

    private void handleButtonSearch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> params = extractParameters(request);
        String city = (String) params.get("city");
        int radius = (int) params.get("radius");

        try {
            List<Hotel> hotels = hotelAPI.fetchHotelsByCity(city, radius,
                    (String) params.get("sortPrice"), (String) params.get("duration"),
                    (String) params.get("activityType"), (String) params.get("priceRange"));
            request.setAttribute("hotels", hotels != null ? hotels : Collections.emptyList());

            if (hotels == null || hotels.isEmpty()) {
                request.setAttribute("errorMessage", "No hotels found for the specified criteria.");
            }

            // Forward to listings.jsp
            request.getRequestDispatcher("/WEB-INF/views/listings.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching hotels for city: " + city, e);
            sendErrorResponse(response);
        }
    }

    private void handleLocationBasedSearch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");

        if (latitude != null && longitude != null) {
            try {
                double lat = Double.parseDouble(latitude);
                double lng = Double.parseDouble(longitude);
                String city = getCityFromCoordinates(lat, lng);

                if (city != null) {
                    List<Hotel> hotels = hotelAPI.fetchHotelsByCity(city, 5000, null, null, null, null);
                    request.setAttribute("hotels", hotels != null ? hotels : Collections.emptyList());

                    if (hotels == null || hotels.isEmpty()) {
                        request.setAttribute("errorMessage", "No hotels found for the specified location.");
                    }

                    request.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(request, response);
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    request.setAttribute("errorMessage", "Unable to determine city from location.");
                    request.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                LOGGER.log(Level.SEVERE, "Invalid latitude or longitude format", e);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                request.setAttribute("errorMessage", "Invalid latitude or longitude format.");
                request.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(request, response);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            prepareDefaultHotelDisplay(request, response);
        }
    }

    private Map<String, Object> extractParameters(HttpServletRequest request) {
        String city = request.getParameter("city");
        String radiusStr = request.getParameter("radius");
        String sortPrice = request.getParameter("sortPrice");
        String duration = request.getParameter("duration");
        String activityType = request.getParameter("activityType");
        String priceRange = request.getParameter("priceRange");

        int radius = (radiusStr != null && !radiusStr.isEmpty()) ? Integer.parseInt(radiusStr) : 5000;

        return Map.of(
                "city", city,
                "radius", radius,
                "sortPrice", sortPrice,
                "duration", duration,
                "activityType", activityType,
                "priceRange", priceRange
        );
    }

    private String getCityFromCoordinates(double latitude, double longitude) {
        String apiKey = dotenv.get("GOOGLE_API_KEY");
        String geocodeUrl = String.format(
                "https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&key=%s",
                latitude, longitude, apiKey
        );

        try {
            HttpResponse<String> response = Unirest.get(geocodeUrl).asString();
            if (response.getStatus() == 200) {
                JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();
                JsonArray results = jsonObject.getAsJsonArray("results");
                if (!results.isEmpty()) {
                    JsonArray addressComponents = results.get(0).getAsJsonObject().getAsJsonArray("address_components");
                    for (JsonElement component : addressComponents) {
                        JsonObject compObj = component.getAsJsonObject();
                        JsonArray types = compObj.getAsJsonArray("types");
                        if (types.contains(new JsonPrimitive("locality"))) {
                            return compObj.get("long_name").getAsString();
                        }
                    }
                }
            } else {
                LOGGER.warning("Failed to retrieve city data: " + response.getStatusText());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching city from coordinates", e);
        }
        return null;
    }

    private void prepareDefaultHotelDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Hotel> hotels = hotelAPI.fetchHotelsByCity("Coventry, UK", 5000, null, null, null, null);
            request.setAttribute("popularHotels", hotels);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch default hotels", e);
            request.setAttribute("errorMessage", "An error occurred while fetching hotels. Please try again.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(gson.toJson(Collections.singletonMap("error", "Unable to fetch hotels due to server error")));
    }
}