package Servlet.HostPanel;

import org.mindrot.jbcrypt.BCrypt;
import utils.DatabaseConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "HostLoginServlet", urlPatterns = {"/hostLogin"})
public class HostLoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(HostLoginServlet.class.getName());

    /**
     * Handles GET requests by forwarding to the HostLogin JSP page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/HostPanel/HostLogin.jsp").forward(request, response);
    }

    /**
     * Handles POST requests for host login.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validate input
        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Email and Password are required.");
            request.getRequestDispatcher("/WEB-INF/views/HostPanel/HostLogin.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            String sql = "SELECT host_id, name, password FROM hosts WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email.trim());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int hostId = rs.getInt("host_id");
                        String hostName = rs.getString("name");
                        String storedPassword = rs.getString("password");

                        if (BCrypt.checkpw(password, storedPassword)) {
                            // Create a session and store host details
                            HttpSession session = request.getSession(true);
                            session.setAttribute("hostId", hostId);
                            session.setAttribute("hostName", hostName);
                            session.setAttribute("hostEmail", email.trim());

                            // Log the successful login
                            LOGGER.info("Host logged in successfully: " + hostName);

                            // Redirect to the HostPanel
                            response.sendRedirect(request.getContextPath() + "/HostPanel");
                        } else {
                            // Invalid password
                            LOGGER.warning("Invalid login attempt for email: " + email);
                            request.setAttribute("errorMessage", "Invalid email or password.");
                            request.getRequestDispatcher("/WEB-INF/views/HostPanel/HostLogin.jsp").forward(request, response);
                        }
                    } else {
                        // Email not found
                        LOGGER.warning("No account found for email: " + email);
                        request.setAttribute("errorMessage", "No account found for the provided email.");
                        request.getRequestDispatcher("/WEB-INF/views/HostPanel/HostLogin.jsp").forward(request, response);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during host login", e);
            request.setAttribute("errorMessage", "An error occurred during login. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/views/HostPanel/HostLogin.jsp").forward(request, response);
        }
    }
}