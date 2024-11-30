<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/navbar.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Booking Confirmation</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f8f9fa;
        }

        .container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 1.5rem;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
        }

        .booking-info {
            background: #f1f1f1;
            border-radius: 10px;
            padding: 20px;
            margin-top: 20px;
        }

        .booking-info p {
            margin: 0.5rem 0;
        }

        footer {
            background-color: #0056b3;
            color: white;
            text-align: center;
            padding: 1rem 0;
        }

        .btn-primary {
            background-color: #FF385C;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            font-size: 16px;
        }

        .btn-primary:hover {
            background-color: #E0314C;
        }
    </style>
</head>
<body>
<main class="container">
    <h2 class="text-center text-primary">Booking Confirmation</h2>
    <c:choose>
        <c:when test="${not empty bookingDetails}">
            <div class="booking-info">
                <p><strong>Booking ID:</strong> ${bookingDetails.bookingId}</p>
                <p><strong>Trip Name:</strong> ${bookingDetails.tripName}</p>
                <p><strong>Number of Participants:</strong> ${bookingDetails.numParticipants}</p>
                <p><strong>Total Price:</strong> £${bookingDetails.totalPrice}</p>
                <p><strong>Booking Date:</strong> ${bookingDetails.bookingDate}</p>
                <p><strong>Start Date:</strong> ${bookingDetails.startDate}</p>
                <p><strong>End Date:</strong> ${bookingDetails.endDate}</p>
                <p><strong>Customer Name:</strong> ${bookingDetails.customerName}</p>
                <p><strong>Customer Email:</strong> ${bookingDetails.customerEmail}</p>
                <p><strong>Special Requests:</strong> ${bookingDetails.specialRequests}</p>
                <p><strong>Cancellation Policy:</strong> ${bookingDetails.cancellationPolicy}</p>
            </div>
            <div class="text-center mt-4">
                <button onclick="location.href='<c:url value='/myTrips' />'" class="btn btn-primary">Go to My Trips</button>
            </div>
            <p class="text-center mt-4">
                Thank you for booking with <strong>AlexTrip</strong>. We hope you have a wonderful experience!
            </p>
        </c:when>
        <c:otherwise>
            <div class="alert alert-warning text-center">
                <strong>No booking details available.</strong> Please check your booking again.
            </div>
        </c:otherwise>
    </c:choose>
</main>
<footer>
    <p>&copy; 2024 AlexTrip. All rights reserved.</p>
</footer>
</body>
</html>