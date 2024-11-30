package Servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "HomePageController", urlPatterns = {"/homepage"})
public class HomePageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set attributes to pass data to homepage.jsp
        request.setAttribute("title", "Welcome to AlexTrip");
        request.setAttribute("subtitle", "Choose your path: Host or Traveler");

        // Forward request to homepage.jsp
        request.getRequestDispatcher("/homepage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // For simplicity, redirect POST requests to GET
        response.sendRedirect(request.getContextPath() + "/homepage");
    }
}