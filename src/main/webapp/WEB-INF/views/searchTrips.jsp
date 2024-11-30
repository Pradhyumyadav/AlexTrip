<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/navbar.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Trips - AlexTrip</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background: linear-gradient(135deg, #6a11cb, #2575fc);
            color: #343a40;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }

        main {
            flex-grow: 1;
        }

        .header-title {
            text-align: center;
            margin-top: 30px;
            margin-bottom: 20px;
            color: white;
            font-size: 2.5rem;
            font-weight: bold;
            text-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2);
        }

        .trip-card {
            background: rgba(255, 255, 255, 0.8);
            border: none;
            border-radius: 12px;
            overflow: hidden;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .trip-card:hover {
            transform: scale(1.02);
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.2);
        }

        .card-img-top {
            height: 200px;
            object-fit: cover;
        }

        .card-body {
            padding: 20px;
        }

        .btn-primary {
            background: linear-gradient(to right, #6a11cb, #2575fc);
            border: none;
            font-size: 16px;
            padding: 10px 20px;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .btn-primary:hover {
            background: linear-gradient(to right, #2575fc, #6a11cb);
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
        }

        .alert-warning {
            background: rgba(255, 165, 0, 0.8);
            border: none;
            color: #fff;
            font-weight: bold;
        }

        footer {
            background-color: #343a40;
            color: white;
            text-align: center;
            padding: 10px 0;
        }
    </style>
</head>
<body>
<main class="container my-4">
    <h1 class="header-title">Search Results</h1>
    <c:choose>
        <c:when test="${not empty trips}">
            <div class="row">
                <c:forEach var="trip" items="${trips}">
                    <div class="col-lg-4 col-md-6 mb-4">
                        <div class="card trip-card">
                            <c:choose>
                                <c:when test="${not empty trip.photoPaths}">
                                    <img src="${trip.photoPaths[0]}" class="card-img-top" alt="${trip.tripName}">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/images/hero1.jpg" class="card-img-top" alt="${trip.tripName}">
                                </c:otherwise>
                            </c:choose>
                            <div class="card-body">
                                <h5 class="card-title">${trip.tripName}</h5>
                                <p class="card-text"><strong>Destination:</strong> ${trip.destination}</p>
                                <p class="card-text"><strong>Duration:</strong> ${trip.duration} days</p>
                                <p class="card-text"><strong>Price:</strong> £${trip.price}</p>
                                <p class="card-text"><strong>Activity Type:</strong> ${trip.activityType}</p>
                                <a href="${pageContext.request.contextPath}/tripDetails?tripId=${trip.tripId}" class="btn btn-primary">View Details</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-warning text-center" role="alert">
                No trips found matching your search criteria. Please try again.
            </div>
        </c:otherwise>
    </c:choose>
</main>

<footer>
    <p>&copy; 2024 AlexTrip. All rights reserved.</p>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>