package Servlet.HostPanel;

import org.mindrot.jbcrypt.BCrypt;
import utils.DatabaseConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "HostSignupServlet", urlPatterns = {"/hostSignup"})
public class HostSignupServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(HostSignupServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to HostSignup.jsp
        request.getRequestDispatcher("/WEB-INF/views/HostPanel/HostSignup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String plainPassword = request.getParameter("password");

        // Validate form input
        if (name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                plainPassword == null || plainPassword.trim().isEmpty()) {
            request.setAttribute("errorMessage", "All fields are required!");
            request.getRequestDispatcher("/WEB-INF/views/HostPanel/HostSignup.jsp").forward(request, response);
            return;
        }

        // Hash the password using BCrypt
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        // Use DatabaseConnectionManager for database operations
        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            String sql = "INSERT INTO hosts (name, email, password, created_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name.trim());
                stmt.setString(2, email.trim());
                stmt.setString(3, hashedPassword);
                stmt.executeUpdate();

                // Redirect to Host Login page after successful signup
                request.setAttribute("successMessage", "Signup successful! Please log in.");
                request.getRequestDispatcher("/WEB-INF/views/HostPanel/HostLogin.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error during host signup", e);
            request.setAttribute("errorMessage", "Signup failed. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/views/HostPanel/HostSignup.jsp").forward(request, response);
        }
    }
}