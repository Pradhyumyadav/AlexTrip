package Servlet;

import Model.Hotel;
import com.google.gson.Gson;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import io.github.cdimascio.dotenv.Dotenv;
import service.HotelAPI;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/searchNearby")
public class SearchServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(SearchServlet.class.getName());
    private final HotelAPI hotelAPI = new HotelAPI();
    private final Gson gson = new Gson();
    private final GeoApiContext geoApiContext;

    public SearchServlet() {
        Dotenv dotenv = Dotenv.configure()
                .directory("/Users/pradhyumyadav/IdeaProjects/AlexTripAgencyManagementSystem/src/main/java/service")
                .load();
        this.geoApiContext = new GeoApiContext.Builder()
                .apiKey(dotenv.get("GOOGLE_API_KEY"))
                .build();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");

        response.setContentType("application/json");

        if (latitude == null || longitude == null) {
            logger.warning("Latitude and longitude are required for nearby search.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(Collections.singletonMap("error", "Location is required to search nearby hotels.")));
            return;
        }

        try {
            // Convert latitude and longitude to city name
            String city = getCityFromCoordinates(Double.parseDouble(latitude), Double.parseDouble(longitude));

            if (city != null && !city.isEmpty()) {
                // Fetch hotels using the city name
                List<Hotel> hotels = hotelAPI.fetchHotelsByCity(city, 5000, null, null, null, null);

                if (hotels == null || hotels.isEmpty()) {
                    logger.info("No hotels found for the specified city.");
                    response.getWriter().write(gson.toJson(Collections.singletonMap("message", "No nearby stays found.")));
                } else {
                    String hotelJson = gson.toJson(hotels);
                    response.getWriter().write(hotelJson);
                }
            } else {
                logger.warning("Unable to determine city from coordinates.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(Collections.singletonMap("error", "Unable to determine city from location.")));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while fetching nearby hotels", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Collections.singletonMap("error", "Unable to fetch hotels due to an internal error.")));
        }
    }

    private String getCityFromCoordinates(double latitude, double longitude) {
        try {
            LatLng location = new LatLng(latitude, longitude);
            GeocodingResult[] results = GeocodingApi.reverseGeocode(geoApiContext, location).await();

            for (GeocodingResult result : results) {
                for (var component : result.addressComponents) {
                    if (component.types[0] == AddressComponentType.LOCALITY) {
                        return component.longName;
                    }
                }
            }
            logger.warning("No locality type found in address components.");
        } catch (ApiException | InterruptedException | IOException e) {
            logger.log(Level.SEVERE, "Error fetching city from coordinates", e);
        }
        return null;
    }
}