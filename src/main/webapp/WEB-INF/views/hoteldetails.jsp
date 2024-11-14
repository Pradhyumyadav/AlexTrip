<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${hotel.name}"/> - AlexTrip</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/main.css" rel="stylesheet">
</head>
<body>
<header>
    <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
        <div class="container">
            <a class="navbar-brand" href="#">AlexTrip</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="#">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">Features</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">Pricing</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</header>

<main class="container my-5 pt-5">
    <c:choose>
        <c:when test="${not empty hotel}">
            <div class="hotel-details">
                <h1 class="display-4">${fn:escapeXml(hotel.name)}</h1>
                <p><strong>Location:</strong> ${fn:escapeXml(hotel.location)}</p>
                <p><strong>Description:</strong> ${fn:escapeXml(hotel.description)}</p>

                <c:if test="${not empty hotel.imageUrls}">
                    <div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">
                        <div class="carousel-inner">
                            <c:forEach var="imageUrl" items="${hotel.imageUrls}" varStatus="status">
                                <div class="carousel-item ${status.first ? 'active' : ''}">
                                    <img src="${imageUrl}" class="d-block w-100" alt="${fn:escapeXml(hotel.name)} image">
                                </div>
                            </c:forEach>
                        </div>
                        <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
                            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Previous</span>
                        </button>
                        <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
                            <span class="carousel-control-next-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Next</span>
                        </button>
                    </div>
                </c:if>

                <div class="mt-4">
                    <h2>Amenities</h2>
                    <ul class="amenities">
                        <c:forEach var="amenity" items="${hotel.amenities}">
                            <li>${fn:escapeXml(amenity)}</li>
                        </c:forEach>
                    </ul>
                </div>

                <div class="mt-4">
                    <h2>Booking Information</h2>
                    <div class="booking-info">
                        <p><strong>Price:</strong> £${hotel.price} per night</p>
                        <p><strong>Rating:</strong> ${hotel.rating} (${hotel.numberReviews} reviews)</p>
                        <a href="${pageContext.request.contextPath}/booking?hotelId=${hotel.id}" class="btn btn-primary mt-3">Reserve Now</a>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-danger" role="alert">
                    ${errorMessage}
            </div>
            <p>Hotel not found. Please check the hotel ID or try again later.</p>
        </c:otherwise>
    </c:choose>
</main>

<footer class="bg-light text-center text-lg-start mt-5">
    <div class="text-center p-3" style="background-color: rgba(0, 0, 0, 0.05);">
        © 2024 AlexTrip: All rights reserved.
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>