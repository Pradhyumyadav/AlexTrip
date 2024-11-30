package Servlet;

import Model.Trip;
import service.TripService;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "SearchTripsController", urlPatterns = {"/searchTrips"})
public class SearchTripsController extends HttpServlet {

    private TripService tripService;

    @Override
    public void init() throws ServletException {
        try {
            // Lookup the DataSource from JNDI
            InitialContext initialContext = new InitialContext();
            DataSource dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/alextrip");
            tripService = new TripService(dataSource);
        } catch (NamingException e) {
            throw new ServletException("Unable to acquire DataSource from JNDI", e);
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
            // Log and handle invalid input formats
            request.setAttribute("error", "Invalid numeric format for duration or max price.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }

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

        // Forward to the search results JSP
        request.getRequestDispatcher("/WEB-INF/views/searchTrips.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirect POST requests to GET method for handling search functionality
        doGet(request, response);
    }
}