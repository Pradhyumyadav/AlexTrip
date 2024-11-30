<%@ page import="Model.Trip" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/navbar.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trip Details - AlexTrip</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background: linear-gradient(135deg, #f8f9fa, #e0e0e0);
            color: #343a40;
        }

        .carousel img {
            object-fit: cover;
            height: 400px;
        }

        .info-section {
            padding: 20px;
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }

        .info-section h5 {
            font-weight: bold;
            color: #343a40;
        }

        .info-section p {
            color: #6c757d;
        }

        .btn-book {
            background: linear-gradient(to right, #6a11cb, #2575fc);
            border: none;
            color: white;
            padding: 10px 20px;
            border-radius: 8px;
            font-size: 16px;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s ease;
        }

        .btn-book:hover {
            background: linear-gradient(to right, #2575fc, #6a11cb);
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
        }

        .card {
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
            padding: 15px;
        }

        footer {
            background-color: #343a40;
            color: white;
            padding: 10px 0;
            text-align: center;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<%
    Trip trip = (Trip) request.getAttribute("trip");
    if (trip != null) {
%>
<main class="container my-4">
    <h1 class="text-center mb-4"><%= trip.getTripName() %></h1>
    <div class="row">
        <div class="col-md-8">
            <!-- Carousel -->
            <div id="tripImagesCarousel" class="carousel slide" data-bs-ride="carousel">
                <div class="carousel-inner">
                    <%
                        List<String> photoPaths = trip.getPhotoPaths();
                        if (photoPaths != null && !photoPaths.isEmpty()) {
                            for (int i = 0; i < photoPaths.size(); i++) {
                    %>
                    <div class="carousel-item <%= i == 0 ? "active" : "" %>">
                        <img src="<%= request.getContextPath() + "/" + photoPaths.get(i) %>" class="d-block w-100" alt="Trip Image">
                    </div>
                    <%
                        }
                    } else {
                    %>
                    <div class="carousel-item active">
                        <img src="<%= request.getContextPath() %>/images/placeholder.jpg" class="d-block w-100" alt="No Image Available">
                    </div>
                    <% } %>
                </div>
                <button class="carousel-control-prev" type="button" data-bs-target="#tripImagesCarousel" data-bs-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Previous</span>
                </button>
                <button class="carousel-control-next" type="button" data-bs-target="#tripImagesCarousel" data-bs-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Next</span>
                </button>
            </div>

            <!-- Trip Description -->
            <div class="info-section mt-4">
                <h5>Description</h5>
                <p><%= trip.getDescription() %></p>
                <h5>Inclusions</h5>
                <p><%= trip.getInclusions() %></p>
                <h5>Exclusions</h5>
                <p><%= trip.getExclusions() %></p>
                <h5>Cancellation Policy</h5>
                <p><%= trip.getCancellationPolicy() %></p>
            </div>
        </div>

        <!-- Trip Details -->
        <div class="col-md-4">
            <div class="card">
                <h5>Trip Information</h5>
                <p><strong>Destination:</strong> <%= trip.getDestination() %></p>
                <p><strong>Price:</strong> £<%= trip.getPrice() %></p>
                <p><strong>Duration:</strong> <%= trip.getDuration() %> days</p>
                <p><strong>Start Date:</strong> <%= trip.getStartDate() %></p>
                <p><strong>End Date:</strong> <%= trip.getEndDate() %></p>
                <p><strong>Max Participants:</strong> <%= trip.getMaxParticipants() %></p>

                <h5 class="mt-4">Host Information</h5>
                <p><strong>Name:</strong> <%= trip.getHostName() %></p>
                <p><strong>Email:</strong> <%= trip.getHostContactEmail() %></p>
                <p><strong>Phone:</strong> <%= trip.getHostContactPhone() %></p>

                <a href="<%= request.getContextPath() %>/bookTrip?tripId=<%= trip.getTripId() %>" class="btn-book mt-3">Book This Trip</a>
            </div>
        </div>
    </div>
</main>
<%
} else {
%>
<main class="container my-4">
    <div class="alert alert-warning text-center">No trip details available.</div>
</main>
<%
    }
%>
<footer>
    <p>&copy; 2024 AlexTrip. All rights reserved.</p>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>