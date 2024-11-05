package service;

import Model.Hotel;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    private final HotelAPI hotelAPI = new HotelAPI(); // Ensure you have an instance of HotelAPI

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String city = request.getParameter("city");
        String radiusStr = request.getParameter("radius");
        String sortPrice = request.getParameter("sortPrice");
        String duration = request.getParameter("duration");
        String activityType = request.getParameter("activityType");
        String priceRange = request.getParameter("priceRange");

        int radius = (radiusStr != null && !radiusStr.isEmpty()) ? Integer.parseInt(radiusStr) : 5000; // Default to 5000 if not specified

        List<Hotel> hotels = hotelAPI.fetchHotelsByCity(city, radius, sortPrice, duration, activityType, priceRange);

        request.setAttribute("hotels", hotels);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/listings.jsp");
        dispatcher.forward(request, response);
    }
}