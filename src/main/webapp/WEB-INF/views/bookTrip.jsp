<%@ page import="java.math.BigDecimal" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/navbar.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Your Trip - AlexTrip</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f8f9fa;
        }

        .container {
            margin-top: 50px;
            margin-bottom: 50px;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }

        .booking-form {
            background: #ffffff;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 6px 10px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 600px;
        }

        .form-control {
            border: 1px solid #ced4da;
            border-radius: 8px;
            height: 45px;
            padding: 10px 15px;
        }

        .form-label {
            font-size: 14px;
            color: #495057;
        }

        .btn-primary {
            background-color: #FF385C;
            border-color: #FF385C;
            font-weight: bold;
            font-size: 16px;
            width: 100%;
            padding: 10px;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .btn-primary:hover {
            background-color: #F73149;
            border-color: #F73149;
        }

        .trip-summary {
            margin-bottom: 20px;
        }

        textarea {
            height: 80px;
        }
    </style>
</head>
<body>
<%
    // Ensure the user is logged in
    if (session.getAttribute("userId") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Fetch trip details passed from the servlet
    String tripName = (String) request.getAttribute("tripName");
    Integer tripDuration = (Integer) request.getAttribute("tripDuration");
    BigDecimal tripPrice = (BigDecimal) request.getAttribute("tripPrice");
    Integer tripId = (Integer) request.getAttribute("tripId");
%>
<div class="container">
    <div class="booking-form">
        <h1 class="text-center mb-4">Book Your Trip</h1>
        <div class="trip-summary">
            <p><strong>Trip Name:</strong> <%= tripName %></p>
            <p><strong>Duration:</strong> <%= tripDuration %> days</p>
            <p><strong>Price per Participant:</strong> £<%= tripPrice %></p>
        </div>
        <form method="post" action="<%= request.getContextPath() %>/bookTrip">
            <!-- Hidden Field for Trip ID -->
            <input type="hidden" name="tripId" value="<%= tripId %>">

            <!-- Number of Participants -->
            <div class="mb-3">
                <label for="numParticipants" class="form-label">Number of Participants</label>
                <input type="number" class="form-control" id="numParticipants" name="numParticipants" required min="1" placeholder="Enter number of participants">
            </div>

            <!-- Customer Name -->
            <div class="mb-3">
                <label for="customerName" class="form-label">Your Name</label>
                <input type="text" class="form-control" id="customerName" name="customerName" required placeholder="Enter your name">
            </div>

            <!-- Customer Email -->
            <div class="mb-3">
                <label for="customerEmail" class="form-label">Email Address</label>
                <input type="email" class="form-control" id="customerEmail" name="customerEmail" required placeholder="Enter your email">
            </div>

            <!-- Customer Phone -->
            <div class="mb-3">
                <label for="customerPhone" class="form-label">Phone Number</label>
                <input type="tel" class="form-control" id="customerPhone" name="customerPhone" required placeholder="Enter your phone number">
            </div>

            <!-- Special Requests -->
            <div class="mb-3">
                <label for="specialRequests" class="form-label">Special Requests</label>
                <textarea class="form-control" id="specialRequests" name="specialRequests" placeholder="Enter any special requests"></textarea>
            </div>

            <!-- Submit Button -->
            <button type="submit" class="btn btn-primary">Confirm Booking</button>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/views/footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>