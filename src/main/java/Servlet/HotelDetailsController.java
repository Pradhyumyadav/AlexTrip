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

@WebServlet("/hotel/details")
public class HotelDetailsController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(HotelDetailsController.class);
    private final HotelAPI hotelAPI;

    public HotelDetailsController() {
        this.hotelAPI = new HotelAPI();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String hotelId = req.getParameter("hotelId");

        if (hotelId == null || hotelId.isEmpty()) {
            logger.warn("Hotel ID is missing in the request.");
            forwardErrorMessage(req, resp, "Invalid hotel ID provided. Please select a valid hotel.");
            return;
        }

        try {
            // Fetch hotel details using the hotel ID
            Hotel hotel = hotelAPI.fetchHotelDetails(hotelId);

            if (hotel == null) {
                logger.warn("No details found for hotel ID: {}", hotelId);
                req.setAttribute("errorMessage", "Hotel not found.");
            } else {
                // Set hotel details as a request attribute
                req.setAttribute("hotel", hotel);
            }

            forwardToJSP(req, resp);

        } catch (Exception e) {
            logger.error("Error retrieving hotel details for hotel ID: {}", hotelId, e);
            forwardErrorMessage(req, resp, "An unexpected error occurred while retrieving hotel details. Please try again later.");
        }
    }

    private void forwardToJSP(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = "/WEB-INF/views/hoteldetails.jsp";  // Ensure this path is correct
        RequestDispatcher dispatcher = req.getRequestDispatcher(path);

        if (dispatcher != null) {
            dispatcher.forward(req, resp);
        } else {
            logger.error("RequestDispatcher could not be created for path: {}", path);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not load hotel details page.");
        }
    }

    private void forwardErrorMessage(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("errorMessage", message);
        forwardToJSP(req, resp);
    }
}