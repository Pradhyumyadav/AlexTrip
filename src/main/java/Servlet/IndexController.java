package Servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Model.Offer;
import Model.Trip;
import service.OfferService;
import service.TripService;
import utils.DatabaseConnectionManager;

@WebServlet(name = "IndexController", urlPatterns = {"/index"})
public class IndexController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(IndexController.class.getName());
    private TripService tripService;
    private OfferService offerService;

    @Override
    public void init() throws ServletException {
        try {
            // Initialize the services using the default constructor
            tripService = new TripService(DatabaseConnectionManager.getDataSource());  // Using DatabaseConnectionManager.getConnection() inside the constructor
            offerService = new OfferService(); // Using DatabaseConnectionManager.getConnection() inside the constructor
            LOGGER.info("Services initialized successfully using DatabaseConnectionManager.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize services", e);
            throw new ServletException("Initialization error", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userEmail") == null) {
            LOGGER.warning("User not logged in, redirecting to login page.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Load trips and offers from the database
            List<Trip> trips = tripService.getAllTrips();
            List<Offer> offers = offerService.getAllActiveOffers();

            // Load activity types for the search panel
            List<String> activityTypes = tripService.getDistinctActivityTypes();

            // Set attributes for JSP
            request.setAttribute("trips", trips);
            request.setAttribute("offers", offers);
            request.setAttribute("activityTypes", activityTypes);

            LOGGER.info("Trips, offers, and activity types loaded successfully.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing request: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "Unable to process request. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/views/errorPage.jsp").forward(request, response);
        }
    }
}