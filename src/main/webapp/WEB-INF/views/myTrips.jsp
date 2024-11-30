<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/navbar.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>My Trips - AlexTrip</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
  <style>
    body {
      font-family: 'Roboto', sans-serif;
      background-color: #f8f9fa;
    }

    .trip-card {
      background: white;
      border-radius: 10px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      margin-bottom: 20px;
      padding: 20px;
      transition: all 0.3s ease;
    }

    .trip-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
    }

    .btn-primary {
      background-color: #0056b3;
      border: none;
    }

    .btn-primary:hover {
      background-color: #003d82;
    }

    .btn-danger {
      background-color: red;
      border: none;
    }

    .btn-danger:hover {
      background-color: darkred;
    }

    footer {
      background-color: #0056b3;
      color: white;
      text-align: center;
      padding: 15px 0;
      margin-top: 30px;
    }
  </style>
</head>
<body>
<main class="container mt-4">
  <h2 class="text-center text-primary">My Booked Trips</h2>
  <c:choose>
    <c:when test="${not empty userBookings}">
      <div class="row mt-4">
        <c:forEach items="${userBookings}" var="trip">
          <div class="col-md-6 col-lg-4">
            <div class="trip-card">
              <h5 class="text-primary">${trip.tripName}</h5>
              <p><strong>Dates:</strong> ${trip.startDate} - ${trip.endDate}</p>
              <p><strong>Participants:</strong> ${trip.numParticipants}</p>
              <p><strong>Total Price:</strong> $${trip.totalPrice}</p>
              <p><strong>Special Requests:</strong> ${trip.specialRequests}</p>
              <form method="post" action="<c:url value='/myTrips' />" class="d-flex align-items-center">
                <input type="hidden" name="bookingId" value="${trip.bookingId}">
                <label>
                  <input type="text" name="specialRequest" value="${trip.specialRequests}" class="form-control me-2" placeholder="Update Special Request">
                </label>
                <button type="submit" name="action" value="update" class="btn btn-primary">Update</button>
              </form>
              <button class="btn btn-danger mt-2" onclick="openModal(${trip.bookingId}, '${trip.cancellationPolicy}')">Cancel Booking</button>
            </div>
          </div>
        </c:forEach>
      </div>
    </c:when>
    <c:otherwise>
      <p class="text-center mt-5">You have no trips booked yet. Start your adventure <a href="<c:url value='/index.jsp' />" class="text-primary">here</a>!</p>
    </c:otherwise>
  </c:choose>
</main>

<footer>
  <p>&copy; 2024 AlexTrip. All rights reserved.</p>
</footer>

<div id="modal" class="modal">
  <div class="modal-content">
    <p id="policyText">Are you sure you want to cancel this booking? Cancellation may result in deductions as per policy.</p>
    <form method="post" action="<c:url value='/myTrips' />">
      <input type="hidden" id="bookingIdInput" name="bookingId">
      <button type="submit" name="action" value="cancel" class="btn btn-primary me-2">Yes, Cancel Booking</button>
      <button type="button" class="btn btn-secondary" onclick="closeModal()">No, Go Back</button>
    </form>
  </div>
</div>

<script>
  function openModal(bookingId, policy) {
    document.getElementById("modal").style.display = "block";
    document.getElementById("bookingIdInput").value = bookingId;
    document.getElementById("policyText").innerText = policy || "Are you sure you want to cancel this booking?";
  }

  function closeModal() {
    document.getElementById("modal").style.display = "none";
  }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>