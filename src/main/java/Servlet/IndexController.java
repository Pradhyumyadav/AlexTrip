package Servlet;

import Model.Hotel;
import service.HotelAPI;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/index")
public class IndexController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(IndexController.class.getName());
    private final HotelAPI hotelAPI;

    public IndexController() {
        this.hotelAPI = new HotelAPI();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String city = request.getParameter("city");
        List<Hotel> hotels;

        try {
            if (city == null || city.isEmpty()) {
                // Default to Coventry, UK if no city is provided
                city = "Coventry, UK";
            }
            hotels = hotelAPI.fetchHotelsByCity(city, 5000, null, null, null, null);

            // Set hotels as an attribute to be accessed in index.jsp
            request.setAttribute("popularHotels", hotels);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching hotels for city: " + city, e);
            request.setAttribute("errorMessage", "An error occurred while fetching hotels. Showing popular destinations in Coventry.");
            hotels = hotelAPI.fetchHotelsByCity("Coventry, UK", 5000, null, null, null, null);
            request.setAttribute("popularHotels", hotels);
        }

        // Forward to index.jsp with the hotels list for server-side rendering
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}