package Servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.mindrot.jbcrypt.BCrypt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@WebServlet(name = "SignupServlet", urlPatterns = {"/signup"})
public class SignupServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SignupServlet.class.getName());
    private DataSource dataSource;
    private Key jwtKey;

    @Override
    public void init() throws ServletException {
        try {
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/alextrip");

            String jwtKeyString = System.getenv("JWT_KEY");
            if (jwtKeyString == null || jwtKeyString.isEmpty()) {
                LOGGER.warning("JWT_KEY not found. Using a default key for development. Replace this in production.");
                jwtKeyString = "default-secret-key-for-dev-only-replace-this";
            }
            jwtKey = Keys.hmacShaKeyFor(jwtKeyString.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Initialization error: ", e);
            throw new ServletException("Initialization error: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/Signup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username").trim();
        String email = request.getParameter("email").trim();
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            response.sendRedirect("signup.jsp?error=missing_fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            response.sendRedirect("signup.jsp?error=password_mismatch");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = dataSource.getConnection()) {
            if (isEmailAlreadyRegistered(email, conn)) {
                response.sendRedirect("signup.jsp?error=duplicate_email");
                return;
            }

            String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, hashedPassword);
                stmt.setString(4, "USER");

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            int userId = rs.getInt(1);
                            String jwt = generateJwt(userId);

                            HttpSession session = request.getSession();
                            session.setAttribute("token", jwt);
                            session.setAttribute("username", username);
                            response.sendRedirect("index.jsp?success=signup_successful");
                        } else {
                            response.sendRedirect("signup.jsp?error=signup_failed");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during signup", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }

    private boolean isEmailAlreadyRegistered(String email, Connection conn) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Email exists
            }
        }
    }

    private String generateJwt(int userId) {
        Instant now = Instant.now();
        Instant exp = now.plus(1, ChronoUnit.HOURS);

        return Jwts.builder()
                .claim(Claims.SUBJECT, String.valueOf(userId)).issuedAt(Date.from(now)).expiration(Date.from(exp))
                .signWith(jwtKey)
                .compact();
    }
}