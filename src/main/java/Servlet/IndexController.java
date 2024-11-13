package Servlet;

import Model.Hotel;
import service.HotelAPI;
import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/index")
public class IndexController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(IndexController.class.getName());
    private final HotelAPI hotelAPI;
    private final Gson gson = new Gson();
    private final Dotenv dotenv = Dotenv.load();

    public IndexController() {
        this.hotelAPI = new HotelAPI();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("GOOGLE_API_KEY", dotenv.get("GOOGLE_API_KEY"));  // Pass API Key to JSP

        String userCity = request.getParameter("userCity");

        if (userCity != null && !userCity.trim().isEmpty()) {
            sendHotelData(response, userCity);
        } else {
            prepareDefaultHotelDisplay(request, response);
        }
    }

    private void sendHotelData(HttpServletResponse response, String userCity) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            List<Hotel> hotels = hotelAPI.fetchHotelsByCity(userCity, 5000, null, null, null, null);
            String jsonHotels = gson.toJson(hotels != null ? hotels : Collections.emptyList());
            response.getWriter().write(jsonHotels);
            LOGGER.info(String.format("Fetched %d hotels for city: %s", hotels != null ? hotels.size() : 0, userCity));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching hotels for city: " + userCity, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Collections.singletonMap("error", "Unable to fetch hotels due to server error")));
        }
    }

    private void prepareDefaultHotelDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Hotel> hotels = hotelAPI.fetchHotelsByCity("Coventry, UK", 5000, null, null, null, null);
            request.setAttribute("popularHotels", hotels);
            if (hotels == null || hotels.isEmpty()) {
                request.setAttribute("errorMessage", "No hotels found for the default location.");
            }
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch default hotels", e);
            request.setAttribute("errorMessage", "An error occurred while fetching hotels. Please try again.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}