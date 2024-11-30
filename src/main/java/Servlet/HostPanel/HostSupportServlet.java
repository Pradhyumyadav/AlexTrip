package Servlet.HostPanel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "HostSupportServlet", urlPatterns = {"/HostSupport"})
public class HostSupportServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward the request to HostSupport.jsp
        request.getRequestDispatcher("/WEB-INF/views/HostPanel/HostSupport.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        // Simulating hard-coded email handling
        String supportEmail = "alextrip@alex.com";
        System.out.println("Support Query Submitted:");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);

        // Send a confirmation message back to the user
        request.setAttribute("confirmationMessage", "Your query has been submitted successfully! Our support team will contact you shortly at " + supportEmail);

        // Forward the user back to the HostSupport.jsp page
        request.getRequestDispatcher("/WEB-INF/views/HostPanel/HostSupport.jsp").forward(request, response);
    }
}