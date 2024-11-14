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

@WebServlet("/hotelDetails")
public class HotelDetailsController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(HotelDetailsController.class);
    private HotelAPI hotelAPI;

    @Override
    public void init() throws ServletException {
        try {
            hotelAPI = new HotelAPI(); // Ensures that HotelAPI is initialized safely
        } catch (Exception e) {
            logger.error("Initialization of HotelAPI failed.", e);
            throw new ServletException("Failed to initialize HotelDetailsController due to HotelAPI initialization failure.", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String hotelId = req.getParameter("hotelId");

        if (hotelId == null || hotelId.trim().isEmpty()) {
            logger.warn("Hotel ID is missing in the request.");
            forwardErrorMessage(req, resp, "Invalid hotel ID provided. Please select a valid hotel.");
            return;
        }

        try {
            Hotel hotel = hotelAPI.fetchHotelDetails(hotelId);
            if (hotel == null) {
                logger.warn("No details found for hotel ID: {}", hotelId);
                forwardErrorMessage(req, resp, "Hotel not found.");
            } else {
                req.setAttribute("hotel", hotel);
                forwardToJSP(req, resp);
            }
        } catch (Exception e) {
            logger.error("Error retrieving hotel details for hotel ID: {}", hotelId, e);
            forwardErrorMessage(req, resp, "An unexpected error occurred while retrieving hotel details. Please try again later.");
        }
    }

    private void forwardToJSP(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/hoteldetails.jsp");
        if (dispatcher != null) {
            dispatcher.forward(req, resp);
        } else {
            logger.error("RequestDispatcher could not be created for path: /WEB-INF/views/hoteldetails.jsp");
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not load the hotel details page.");
        }
    }

    private void forwardErrorMessage(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("errorMessage", message);
        forwardToJSP(req, resp);
    }
}