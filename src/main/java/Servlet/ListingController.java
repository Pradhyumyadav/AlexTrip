package Servlet;

import Model.Hotel;
import service.HotelAPI;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/listings")
public class ListingController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ListingController.class.getName());
    private HotelAPI hotelAPI;

    @Override
    public void init() throws ServletException {
        try {
            this.hotelAPI = new HotelAPI();  // Ensure HotelAPI is correctly initialized
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize HotelAPI: " + e.getMessage(), e);
            throw new ServletException("Initialization error in ListingController: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Getting and sanitizing input from the user
            String cityName = sanitizeInput(req.getParameter("city"));
            String radiusStr = sanitizeInput(req.getParameter("radius"));
            String activityType = sanitizeInput(req.getParameter("activityType"));
            String sortPrice = sanitizeInput(req.getParameter("sortPrice"));
            String duration = sanitizeInput(req.getParameter("duration"));
            String priceRange = sanitizeInput(req.getParameter("priceRange"));

            LOGGER.info(String.format("Received parameters - City: %s, Radius: %s, ActivityType: %s, SortPrice: %s, Duration: %s, PriceRange: %s",
                    cityName, radiusStr, activityType, sortPrice, duration, priceRange));

            if (cityName.isEmpty()) {
                forwardErrorMessage(req, resp, "City name is required to search for hotels.");
                return;
            }

            int radius = parseInteger(radiusStr);

            // Call HotelAPI to fetch the hotels
            List<Hotel> hotels = hotelAPI.fetchHotelsByCity(cityName, radius, sortPrice, duration, activityType, priceRange);

            // Set hotels to the request attribute to be used in JSP
            req.setAttribute("hotels", hotels != null ? hotels : Collections.emptyList());

            if (hotels == null || hotels.isEmpty()) {
                req.setAttribute("errorMessage", "No hotels found matching the specified criteria.");
                LOGGER.warning("No hotels found for the given parameters.");
            } else {
                LOGGER.info(String.format("Fetched %d hotels for city: %s", hotels.size(), cityName));
            }

            // Forward to JSP page to display hotels
            forwardToJSP(req, resp);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while fetching hotels", e);
            forwardErrorMessage(req, resp, "An unexpected error occurred while processing your request. Please try again later.");
        }
    }

    private void forwardToJSP(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/listings.jsp").forward(req, resp);
    }

    private void forwardErrorMessage(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("errorMessage", message);
        req.setAttribute("hotels", Collections.emptyList());  // In case of error, return an empty list
        forwardToJSP(req, resp);
    }

    private String sanitizeInput(String input) {
        // Sanitize input to avoid SQL injection or other issues
        return input == null ? "" : input.trim().replaceAll("[^a-zA-Z0-9\\s-]", "");
    }

    private int parseInteger(String value) {
        // Handle case where radius is not a valid integer
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid integer format for radius: " + value + ". Using default radius: 5000");
            return 5000; // Default radius
        }
    }
}