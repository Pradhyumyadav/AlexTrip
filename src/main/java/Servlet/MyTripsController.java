package Servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "MyTripsController", urlPatterns = {"/myTrips"})
public class MyTripsController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(MyTripsController.class);
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/alextrip");
            logger.info("DataSource initialized successfully.");
        } catch (NamingException e) {
            logger.error("Database connection problem during initialization", e);
            throw new ServletException("Database connection problem", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            logger.info("Unauthorized access to MyTrips. Redirecting to login page.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");

        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT b.booking_id, b.num_participants, b.booking_date, b.customer_name, b.customer_email, " +
                    "b.special_requests, b.total_price, t.destination AS trip_name, t.start_date, t.end_date, " +
                    "t.cancellation_policy, t.photos, t.description AS trip_description, t.activity_type " +
                    "FROM bookings b " +
                    "JOIN trips t ON b.trip_id = t.trip_id " +
                    "WHERE b.user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    List<BookingDetails> myTrips = new ArrayList<>();
                    while (rs.next()) {
                        List<String> photoPaths = new ArrayList<>();
                        if (rs.getString("photos") != null && !rs.getString("photos").isEmpty()) {
                            photoPaths = Arrays.asList(rs.getString("photos").split(","));
                        }
                        BookingDetails booking = new BookingDetails(
                                rs.getInt("booking_id"),
                                rs.getInt("num_participants"),
                                rs.getDate("booking_date"),
                                rs.getString("customer_name"),
                                rs.getString("customer_email"),
                                rs.getString("special_requests"),
                                rs.getDouble("total_price"),
                                rs.getString("trip_name"),
                                rs.getDate("start_date"),
                                rs.getDate("end_date"),
                                rs.getString("cancellation_policy"),
                                photoPaths,
                                rs.getString("trip_description"),
                                rs.getString("activity_type")
                        );
                        myTrips.add(booking);
                    }
                    request.setAttribute("userBookings", myTrips);
                    logger.info("Fetched {} trips for user ID: {}", myTrips.size(), userId);
                    request.getRequestDispatcher("/WEB-INF/views/myTrips.jsp").forward(request, response);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching trip details for user ID: {}", userId, e);
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            logger.info("Unauthorized POST access to MyTrips. Redirecting to login page.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        switch (action) {
            case "update":
                handleUpdateAction(request);
                break;
            case "cancel":
                handleCancelAction(request);
                break;
            default:
                logger.warn("Unknown action received: {}", action);
                break;
        }

        response.sendRedirect(request.getContextPath() + "/myTrips");
    }

    private void handleUpdateAction(HttpServletRequest request) {
        String bookingId = request.getParameter("bookingId");
        String specialRequest = request.getParameter("specialRequest");

        if (bookingId == null || specialRequest == null) {
            logger.warn("Missing parameters for updating special request.");
            return;
        }

        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE bookings SET special_requests = ? WHERE booking_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, specialRequest.trim());
                stmt.setInt(2, Integer.parseInt(bookingId));
                int rowsUpdated = stmt.executeUpdate();
                logger.info("Updated {} row(s) for booking ID: {}", rowsUpdated, bookingId);
            }
        } catch (SQLException e) {
            logger.error("Error updating special request for booking ID: {}", bookingId, e);
        }
    }

    private void handleCancelAction(HttpServletRequest request) {
        String bookingId = request.getParameter("bookingId");

        if (bookingId == null) {
            logger.warn("Missing booking ID for cancellation.");
            return;
        }

        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE FROM bookings WHERE booking_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(bookingId));
                int rowsDeleted = stmt.executeUpdate();
                logger.info("Cancelled booking ID: {}, {} row(s) deleted.", bookingId, rowsDeleted);
            }
        } catch (SQLException e) {
            logger.error("Error cancelling booking ID: {}", bookingId, e);
        }
    }
}