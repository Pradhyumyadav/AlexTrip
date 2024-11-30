package Servlet;

import utils.DatabaseConnectionManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "BookingConfirmationController", urlPatterns = {"/bookingConfirmation"})
public class BookingConfirmationController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String bookingId = request.getParameter("bookingId");
        if (bookingId == null || bookingId.isEmpty()) {
            response.sendRedirect("error.jsp");
            return;
        }

        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            String sql = "SELECT b.booking_id, b.num_participants, b.booking_date, b.total_price, b.special_requests, " +
                    "t.destination AS trip_name, t.start_date, t.end_date, t.cancellation_policy, b.customer_name, " +
                    "b.customer_email, t.photos AS photo_paths, t.description AS trip_description, t.activity_type " +
                    "FROM bookings b " +
                    "JOIN trips t ON b.trip_id = t.trip_id " +
                    "WHERE b.booking_id = ? AND b.user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(bookingId));
                stmt.setInt(2, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        List<String> photoPaths = null;
                        String photoPathsString = rs.getString("photo_paths");
                        if (photoPathsString != null && !photoPathsString.isEmpty()) {
                            photoPaths = Arrays.asList(photoPathsString.split(","));
                        }

                        BookingDetails bookingDetails = new BookingDetails(
                                rs.getInt("booking_id"),
                                rs.getInt("num_participants"),
                                rs.getDate("booking_date"),
                                rs.getString("customer_name"), // Customer Name
                                rs.getString("customer_email"), // Customer Email
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

                        request.setAttribute("bookingDetails", bookingDetails);
                        request.getRequestDispatcher("/WEB-INF/views/bookingConfirmation.jsp").forward(request, response);
                    } else {
                        response.sendRedirect("error.jsp");
                    }
                }
            }
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error while fetching booking details.");
        }
    }
}