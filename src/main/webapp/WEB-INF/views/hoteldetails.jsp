<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${hotel.name}"/> - AlexTrip</title>
    <!-- Include Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container my-5">
    <c:choose>
        <c:when test="${not empty hotel}">
            <h1>${fn:escapeXml(hotel.name)}</h1>
            <p><strong>Location:</strong> ${fn:escapeXml(hotel.location)}</p>
            <p><strong>Description:</strong> ${fn:escapeXml(hotel.description)}</p>
            <!-- Other hotel details -->
            <c:if test="${not empty hotel.imageUrls}">
                <div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">
                    <div class="carousel-inner">
                        <c:forEach var="imageUrl" items="${hotel.imageUrls}" varStatus="status">
                            <div class="carousel-item ${status.first ? 'active' : ''}">
                                <img src="${imageUrl}" class="d-block w-100" alt="${fn:escapeXml(hotel.name)} image">
                            </div>
                        </c:forEach>
                    </div>
                    <!-- Carousel controls -->
                    <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators"
                            data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators"
                            data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Next</span>
                    </button>
                </div>
            </c:if>
            <!-- Reserve button -->
            <a href="${pageContext.request.contextPath}/booking?hotelId=${hotel.id}" class="btn btn-primary mt-3">Reserve</a>
        </c:when>
        <c:otherwise>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert">
                        ${errorMessage}
                </div>
            </c:if>
            <p>Hotel not found. Please check the hotel ID or try again later.</p>
        </c:otherwise>
    </c:choose>
</div>
<!-- Include Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>