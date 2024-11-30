package Servlet;

import Model.Trip;
import service.TripService;
import utils.DatabaseConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "SearchTripsController", urlPatterns = {"/searchTrips"})
public class SearchTripsController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SearchTripsController.class.getName());
    private TripService tripService;

    @Override
    public void init() throws ServletException {
        try {
            // Use DatabaseConnectionManager to get the DataSource
            tripService = new TripService(DatabaseConnectionManager.getDataSource());
            LOGGER.info("TripService initialized successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize TripService", e);
            throw new ServletException("Unable to initialize TripService", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Extract query parameters from the request
        String destination = request.getParameter("destination");
        String durationStr = request.getParameter("duration");
        String priceStr = request.getParameter("maxPrice");
        String activityType = request.getParameter("activityType");

        // Initialize variables
        Integer duration = null;
        BigDecimal maxPrice = null;

        try {
            // Parse numeric parameters
            if (durationStr != null && !durationStr.trim().isEmpty()) {
                duration = Integer.parseInt(durationStr.trim());
            }
            if (priceStr != null && !priceStr.trim().isEmpty()) {
                maxPrice = new BigDecimal(priceStr.trim());
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid numeric format for duration or max price", e);
            request.setAttribute("error", "Invalid numeric format for duration or max price.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }

        try {
            // Fetch trips based on search criteria
            List<Trip> trips = tripService.searchTrips(
                    destination != null ? destination.trim() : null,
                    duration,
                    maxPrice,
                    activityType != null ? activityType.trim() : null
            );

            // Set attributes for the view
            request.setAttribute("trips", trips);
            request.setAttribute("destination", destination);
            request.setAttribute("duration", duration);
            request.setAttribute("maxPrice", maxPrice);
            request.setAttribute("activityType", activityType);

            // Handle no result scenario
            if (trips.isEmpty()) {
                request.setAttribute("message", "No trips found matching your search criteria. Try different filters.");
            }

            LOGGER.info("Search results loaded successfully.");
            // Forward to the search results JSP
            request.getRequestDispatcher("/WEB-INF/views/searchTrips.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while searching for trips", e);
            request.setAttribute("errorMessage", "Unable to process your request. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirect POST requests to GET method for handling search functionality
        doGet(request, response);
    }
}