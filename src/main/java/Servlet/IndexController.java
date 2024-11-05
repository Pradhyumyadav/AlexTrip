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
            // Check if city is provided; if not, use default value
            if (city == null || city.isEmpty()) {
                city = "Coventry, UK";
            }
            // Fetch hotels based on city
            hotels = hotelAPI.fetchHotelsByCity(city, 5000, null, null, null, null);

            // Set the list of hotels in the request scope
            request.setAttribute("popularHotels", hotels);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching hotels for city: " + city, e);

            // Set an error message if fetching hotels fails and provide default hotels for Coventry
            request.setAttribute("errorMessage", "An error occurred while fetching hotels. Showing popular destinations in Coventry.");
            hotels = hotelAPI.fetchHotelsByCity("Coventry, UK", 5000, null, null, null, null);
            request.setAttribute("popularHotels", hotels);
        }

        // Define and set predefined activity types list for display in the dropdown
        List<String> activityTypes = List.of("lodging", "hotel", "motel", "bed_and_breakfast", "hostel", "resort", "guest_house", "rv_park", "campground");
        request.setAttribute("activityTypes", activityTypes);

        // Forward to index.jsp with hotels and activity types data
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}