<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AlexTrip - Discover Your Next Adventure</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <style>
        body, html {
            margin: 0;
            padding: 0;
            font-family: 'Roboto', sans-serif;
            overflow-x: hidden;
            background: linear-gradient(to bottom, #1e3c72, #2a5298);
            color: white;
        }

        .hero-section {
            height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            text-align: center;
        }

        .hero-heading {
            font-size: 3.5rem;
            font-weight: bold;
            margin-bottom: 20px;
            text-shadow: 2px 2px 6px rgba(0, 0, 0, 0.6);
        }

        .search-panel {
            background: rgba(255, 255, 255, 0.9);
            padding: 30px;
            border-radius: 15px;
            max-width: 700px;
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
        }

        .btn-primary {
            background-color: #ff5722;
            border: none;
        }

        .btn-primary:hover {
            background-color: #e64a19;
        }

        .section-header {
            text-align: center;
            margin: 50px 0;
            font-weight: bold;
            color: white;
            font-size: 1.8rem;
        }

        .card {
            border: none;
            border-radius: 10px;
            transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
        }

        .card:hover {
            transform: scale(1.03);
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
        }

        .card-img-top {
            border-top-left-radius: 10px;
            border-top-right-radius: 10px;
            height: 200px;
            object-fit: cover;
        }

        footer {
            background-color: #333;
            color: #fff;
            padding: 15px 0;
            text-align: center;
        }
    </style>
</head>
<body>
<%@ include file="/WEB-INF/views/navbar.jsp" %>

<!-- Hero Section -->
<div class="hero-section">
    <h1 class="hero-heading">Discover Your Next Adventure</h1>
    <div class="search-panel">
        <form class="d-flex flex-wrap" method="get" action="${pageContext.request.contextPath}/searchTrips">
            <input type="text" class="form-control me-2 mb-2" name="destination" placeholder="Destination" aria-label="Destination">
            <input type="number" class="form-control me-2 mb-2" name="duration" placeholder="Duration (Days)" aria-label="Duration">
            <input type="number" class="form-control me-2 mb-2" name="maxPrice" placeholder="Max Price (£)" aria-label="Max Price">
            <select class="form-control me-2 mb-2" name="activityType" aria-label="Activity Type">
                <option value="" disabled selected>Activity Type</option>
                <c:forEach items="${activityTypes}" var="activityType">
                    <option value="${activityType}">${activityType}</option>
                </c:forEach>
            </select>
            <button class="btn btn-primary mb-2" type="submit">Search</button>
        </form>
    </div>
</div>

<!-- Exclusive Offers Section -->
<div class="container my-5">
    <h2 class="section-header">Exclusive Offers</h2>
    <div class="row">
        <c:forEach items="${offers}" var="offer">
            <div class="col-lg-4 col-md-6 mb-4">
                <div class="card">
                    <img src="${offer.photoPaths[0]}" class="card-img-top" alt="${offer.details}">
                    <div class="card-body">
                        <h5 class="card-title">${offer.details}</h5>
                        <p class="card-text">Discounted Price: £${offer.discountedPrice}</p>
                        <a href="${pageContext.request.contextPath}/tripDetails?tripId=${offer.tripId}" class="btn btn-primary">Learn More</a>
                    </div>
                </div>
            </div>
        </c:forEach>
        <c:if test="${empty offers}">
            <div class="col-12 text-center"><p>No offers available at the moment.</p></div>
        </c:if>
    </div>
</div>

<!-- Popular Trips Section -->
<div class="container my-5">
    <h2 class="section-header">Popular Trips</h2>
    <div class="row">
        <c:forEach items="${trips}" var="trip">
            <div class="col-lg-4 col-md-6 mb-4">
                <div class="card">
                    <img src="${trip.photoPaths[0]}" class="card-img-top" alt="${trip.tripName}">
                    <div class="card-body">
                        <h5 class="card-title">${trip.tripName}</h5>
                        <p class="card-text">${trip.description}</p>
                        <p class="card-text">From £${trip.price}</p>
                        <a href="${pageContext.request.contextPath}/tripDetails?tripId=${trip.tripId}" class="btn btn-primary">View Trip</a>
                    </div>
                </div>
            </div>
        </c:forEach>
        <c:if test="${empty trips}">
            <div class="col-12 text-center"><p>No trips are available at the moment. Check back later!</p></div>
        </c:if>
    </div>
</div>

<!-- Footer -->
<footer>
    <p>&copy; 2024 AlexTrip. All rights reserved.</p>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>