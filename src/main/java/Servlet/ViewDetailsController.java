package Servlet;

import Model.Trip;
import service.TripService;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ViewDetailsController", urlPatterns = {"/tripDetails"})
public class ViewDetailsController extends HttpServlet {

    private TripService tripService;
    private static final Logger LOGGER = Logger.getLogger(ViewDetailsController.class.getName());

    @Override
    public void init() throws ServletException {
        try {
            // Initialize DataSource and TripService
            InitialContext initialContext = new InitialContext();
            DataSource dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/alextrip");
            tripService = new TripService(dataSource);
            LOGGER.info("ViewDetailsController initialized successfully.");
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, "Error initializing ViewDetailsController", e);
            throw new ServletException("Initialization failed", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tripIdParam = request.getParameter("tripId");

        // Validate tripId parameter
        if (tripIdParam == null || tripIdParam.isEmpty()) {
            LOGGER.warning("Trip ID parameter is missing or empty.");
            response.sendRedirect("errorPage.jsp");
            return;
        }

        int tripId;
        try {
            tripId = Integer.parseInt(tripIdParam);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid trip ID: " + tripIdParam, e);
            response.sendRedirect("errorPage.jsp");
            return;
        }

        // Fetch trip details
        Trip trip = tripService.getTripById(tripId);
        if (trip == null) {
            LOGGER.info("No trip found with ID: " + tripId);
            response.sendRedirect("errorPage.jsp");
            return;
        }

        // Set trip details in the request and forward to JSP
        request.setAttribute("trip", trip);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/viewDetails.jsp");
        dispatcher.forward(request, response);
    }
}