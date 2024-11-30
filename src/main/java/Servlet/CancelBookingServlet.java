package Servlet;

import utils.DatabaseConnectionManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "CancelBookingServlet", urlPatterns = {"/cancelBooking"})
public class CancelBookingServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String bookingId = request.getParameter("bookingId");
        String confirmCancellation = request.getParameter("confirmCancellation");

        // Check if booking ID is valid
        if (bookingId == null || bookingId.isEmpty()) {
            request.setAttribute("errorMessage", "Missing or invalid booking ID.");
            request.getRequestDispatcher("/WEB-INF/views/myTrips.jsp").forward(request, response);
            return;
        }

        // Check if the user confirmed the cancellation
        if (confirmCancellation == null || !confirmCancellation.equals("yes")) {
            request.setAttribute("errorMessage", "You must confirm the cancellation.");
            request.getRequestDispatcher("/WEB-INF/views/myTrips.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            String sql = "DELETE FROM bookings WHERE booking_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(bookingId));
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    request.setAttribute("successMessage", "Booking cancelled successfully.");
                } else {
                    request.setAttribute("errorMessage", "No booking found with the given ID.");
                }
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error cancelling booking: " + e.getMessage());
        }

        // Redirect to myTrips.jsp
        request.getRequestDispatcher("/WEB-INF/views/myTrips.jsp").forward(request, response);
    }
}