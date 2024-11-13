<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, java.util.logging.*" %>

<%
    Logger logger = Logger.getLogger("BookingLogger");

    String hotelId = request.getParameter("hotelId");
    String checkinDate = request.getParameter("checkin");
    String checkoutDate = request.getParameter("checkout");
    String roomType = request.getParameter("roomType");
    String customerName = request.getParameter("name");
    String customerEmail = request.getParameter("email");
    String customerPhone = request.getParameter("phone");
    String totalPrice = request.getParameter("totalPrice");
    String userId = request.getParameter("userId");

    if (hotelId == null || userId == null || checkinDate == null || checkoutDate == null ||
            customerName == null || customerEmail == null || roomType == null || totalPrice == null) {
        out.println("<p>Error: All fields are required.</p>");
        return;
    }

    String url = "jdbc:postgresql://localhost:5432/AlexTrip";
    String dbUser = "postgres";
    String dbPassword = "123";

    String sql = "INSERT INTO bookings (hotel_id, user_id, checkin_date, checkout_date, customer_name, " +
            "customer_email, customer_phone, room_type, total_price, booking_date) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

    try (
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            PreparedStatement pstmt = conn.prepareStatement(sql);
    ) {
        pstmt.setInt(1, Integer.parseInt(hotelId));
        pstmt.setInt(2, Integer.parseInt(userId));
        pstmt.setDate(3, Date.valueOf(checkinDate));
        pstmt.setDate(4, Date.valueOf(checkoutDate));
        pstmt.setString(5, customerName);
        pstmt.setString(6, customerEmail);
        pstmt.setString(7, customerPhone);
        pstmt.setString(8, roomType);
        pstmt.setBigDecimal(9, new java.math.BigDecimal(totalPrice));

        int rowsInserted = pstmt.executeUpdate();
        if (rowsInserted > 0) {
            out.println("<div class='alert alert-success'>Booking successful!</div>");
        } else {
            out.println("<div class='alert alert-danger'>Booking failed. Please try again.</div>");
        }

    } catch (SQLException e) {
        logger.log(Level.SEVERE, "SQL Exception during booking", e);
        out.println("<div class='alert alert-danger'>Error: " + e.getMessage() + "</div>");
    } catch (NumberFormatException e) {
        logger.log(Level.SEVERE, "Invalid input for hotelId, userId, or totalPrice", e);
        out.println("<div class='alert alert-danger'>Error: Invalid input format.</div>");
    }
%>