package Servlet;

import Model.Hotel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.HotelAPI;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet("/listings")
public class ListingController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ListingController.class);
    private final HotelAPI hotelAPI;

    public ListingController() {
        this.hotelAPI = new HotelAPI();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Retrieve and sanitize parameters
            String cityName = sanitizeInput(req.getParameter("city"));
            String radiusStr = sanitizeInput(req.getParameter("radius"));
            String activityType = sanitizeInput(req.getParameter("activityType"));
            String sortPrice = sanitizeInput(req.getParameter("sortPrice"));
            String duration = sanitizeInput(req.getParameter("duration"));
            String priceRange = sanitizeInput(req.getParameter("priceRange"));

            // Log the parameters for debugging
            logger.info("Received parameters - City: {}, Radius: {}, ActivityType: {}, SortPrice: {}, Duration: {}, PriceRange: {}",
                    cityName, radiusStr, activityType, sortPrice, duration, priceRange);

            // Validate required parameters
            if (isNullOrEmpty(cityName)) {
                forwardErrorMessage(req, resp, "City name is required to search for hotels.");
                return;
            }

            // Parse radius, default to 5000 if invalid
            int radius = parseInteger(radiusStr);

            // Fetch hotels based on parameters
            List<Hotel> hotels = hotelAPI.fetchHotelsByCity(cityName, radius, sortPrice, duration, activityType, priceRange);
            req.setAttribute("hotels", hotels); // Always set hotels

            // Log result count and handle cases where no hotels are found
            if (hotels.isEmpty()) {
                req.setAttribute("errorMessage", "No hotels found matching the specified criteria.");
                logger.warn("No hotels found for the given parameters.");
            } else {
                logger.info("Fetched {} hotels for city: {}", hotels.size(), cityName);
            }

            // Forward to listings.jsp
            forwardToJSP(req, resp);

        } catch (Exception e) {
            logger.error("Error while fetching hotels", e);
            forwardErrorMessage(req, resp, "An unexpected error occurred while processing your request. Please try again later.");
        }
    }

    private void forwardToJSP(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/listings.jsp");
        dispatcher.forward(req, resp);
    }

    private void forwardErrorMessage(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("errorMessage", message);
        req.setAttribute("hotels", Collections.emptyList()); // Ensure an empty list for safety
        forwardToJSP(req, resp);
    }

    private String sanitizeInput(String input) {
        return input == null ? "" : input.replaceAll("[^a-zA-Z0-9\\s-]", "").trim();
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private int parseInteger(String value) {
        final int defaultRadius = 5000; // Define a constant for clarity

        if (isNullOrEmpty(value)) {
            logger.warn("Radius parameter is missing or empty. Using default radius: {}", defaultRadius);
            return defaultRadius;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer format for radius: {}. Using default radius: {}", value, defaultRadius);
            return defaultRadius;
        }
    }
}