package Servlet;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "BookTripServlet", urlPatterns = {"/bookTrip"})
public class BookTripServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/alextrip");
        } catch (NamingException e) {
            throw new ServletException("Database connection problem", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tripId = request.getParameter("tripId");
        if (tripId == null || tripId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid trip ID");
            return;
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT trip_id, trip_name, duration, price FROM trips WHERE trip_id = ?")) {
            stmt.setInt(1, Integer.parseInt(tripId));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    request.setAttribute("tripId", rs.getInt("trip_id"));
                    request.setAttribute("tripName", rs.getString("trip_name"));
                    request.setAttribute("tripDuration", rs.getInt("duration"));
                    request.setAttribute("tripPrice", rs.getBigDecimal("price"));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Trip not found.");
                    return;
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database error while fetching trip details.", e);
        }

        request.getRequestDispatcher("/WEB-INF/views/bookTrip.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        String tripId = request.getParameter("tripId");
        String numParticipantsStr = request.getParameter("numParticipants");
        String customerName = request.getParameter("customerName");
        String customerEmail = request.getParameter("customerEmail");
        String customerPhone = request.getParameter("customerPhone");
        String specialRequests = request.getParameter("specialRequests");

        if (tripId == null || numParticipantsStr == null || customerName == null || customerEmail == null || customerPhone == null ||
                tripId.isEmpty() || numParticipantsStr.isEmpty() || customerName.isEmpty() || customerEmail.isEmpty() || customerPhone.isEmpty()) {
            request.setAttribute("errorMessage", "Required fields are missing.");
            request.getRequestDispatcher("/WEB-INF/views/bookTrip.jsp").forward(request, response);
            return;
        }

        try {
            int numParticipants = Integer.parseInt(numParticipantsStr);
            double totalPrice = calculateTotalPrice(tripId, numParticipants);
            int bookingId = insertBooking(tripId, userId, numParticipants, customerName, customerEmail, customerPhone, specialRequests, totalPrice);

            // Save the booking ID to the session for future retrieval (e.g., for MyTrips page)
            session.setAttribute("latestBookingId", bookingId);

            response.sendRedirect("bookingConfirmation?bookingId=" + bookingId);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid number format for participants.");
            request.getRequestDispatcher("/WEB-INF/views/bookTrip.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/bookTrip.jsp").forward(request, response);
        }
    }

    private double calculateTotalPrice(String tripId, int numParticipants) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT price FROM trips WHERE trip_id = ?")) {
            stmt.setInt(1, Integer.parseInt(tripId));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("price") * numParticipants;
                } else {
                    throw new SQLException("No trip found with ID: " + tripId);
                }
            }
        }
    }

    private int insertBooking(String tripId, int userId, int numParticipants, String customerName, String customerEmail, String customerPhone, String specialRequests, double totalPrice) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO bookings (trip_id, user_id, num_participants, customer_name, customer_email, customer_phone, special_requests, total_price, booking_date) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, Integer.parseInt(tripId));
            stmt.setInt(2, userId);
            stmt.setInt(3, numParticipants);
            stmt.setString(4, customerName);
            stmt.setString(5, customerEmail);
            stmt.setString(6, customerPhone);
            stmt.setString(7, specialRequests);
            stmt.setDouble(8, totalPrice);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating booking failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating booking failed, no ID obtained.");
                }
            }
        }
    }
}