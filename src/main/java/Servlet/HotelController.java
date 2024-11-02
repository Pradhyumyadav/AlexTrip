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
import java.util.List;

@WebServlet("/hotels")
public class HotelController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(HotelController.class);
    private final HotelAPI hotelAPI;

    public HotelController() {
        this.hotelAPI = new HotelAPI();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Fetch parameters
            String cityName = sanitizeInput(req.getParameter("location"));
            String radiusStr = req.getParameter("radius");
            String activityType = sanitizeInput(req.getParameter("activityType"));
            String sortPrice = sanitizeInput(req.getParameter("sortPrice"));
            String duration = sanitizeInput(req.getParameter("duration"));

            // Check required parameters
            if (isNullOrEmpty(cityName)) {
                forwardErrorMessage(req, resp, "City name parameter is required.");
                return;
            }

            int radius = parseInteger(radiusStr, 5) * 1000; // Default radius in meters

            logger.info("Fetching hotels for - City: {}, Radius: {}, ActivityType: {}, SortPrice: {}, Duration: {}",
                    cityName, radius, activityType, sortPrice, duration);

            // Fetch hotel list
            List<Hotel> hotels = hotelAPI.fetchHotelsByCity(cityName, radius, sortPrice, duration, activityType);

            if (hotels.isEmpty()) {
                forwardErrorMessage(req, resp, "No hotels found for the specified parameters.");
                return;
            }

            // Set attributes for JSP
            req.setAttribute("hotels", hotels);

            // Forward to listings.jsp for rendering
            forwardToJSP(req, resp);

        } catch (Exception e) {
            logger.error("Error fetching hotels", e);
            forwardErrorMessage(req, resp, "An unexpected error occurred. Please try again later.");
        }
    }

    private void forwardToJSP(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/listings.jsp");
        dispatcher.forward(req, resp);
    }

    private void forwardErrorMessage(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("errorMessage", message);
        forwardToJSP(req, resp);
    }

    private String sanitizeInput(String input) {
        return input == null ? null : input.replaceAll("[^a-zA-Z0-9\\s-]", "");
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private int parseInteger(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value: {}. Using default: {}", value, defaultValue);
            return defaultValue;
        }
    }
}