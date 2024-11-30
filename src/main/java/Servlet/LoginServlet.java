package Servlet;

import org.mindrot.jbcrypt.BCrypt;
import utils.DatabaseConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward GET requests to the login page
        request.getRequestDispatcher("/WEB-INF/views/Login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Email and Password are required and cannot be blank.");
            request.getRequestDispatcher("/WEB-INF/views/Login.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            String sql = "SELECT user_id, username, password, role FROM users WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email.trim());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int userId = rs.getInt("user_id");
                        String username = rs.getString("username");
                        String storedHash = rs.getString("password");
                        String role = rs.getString("role");

                        if (BCrypt.checkpw(password, storedHash)) {
                            // Create a session and store user details
                            HttpSession session = request.getSession(true); // Ensures a session is always created
                            session.setAttribute("userId", userId);
                            session.setAttribute("username", username);
                            session.setAttribute("userEmail", email.trim());
                            session.setAttribute("userRole", role);

                            // Redirect to /index after successful login
                            response.sendRedirect(request.getContextPath() + "/index");
                        } else {
                            // Invalid password
                            request.setAttribute("errorMessage", "Invalid email or password.");
                            request.getRequestDispatcher("/WEB-INF/views/Login.jsp").forward(request, response);
                        }
                    } else {
                        // Email not found
                        request.setAttribute("errorMessage", "User not found.");
                        request.getRequestDispatcher("/WEB-INF/views/Login.jsp").forward(request, response);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database error during login", e);
            throw new ServletException("Database error during login", e);
        }
    }
}