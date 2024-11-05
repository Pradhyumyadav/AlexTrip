<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${hotel.name}"/> - AlexTrip</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container my-5">
    <c:choose>
        <c:when test="${not empty hotel}">
            <h1>${fn:escapeXml(hotel.name)}</h1>
            <p><strong>Address:</strong> <c:out value="${hotel.address}"/>, <c:out value="${hotel.city}"/>, <c:out value="${hotel.country}"/></p>
            <p><strong>Description:</strong> <c:out value="${hotel.description != null ? hotel.description : 'N/A'}"/></p>
            <p><strong>Amenities:</strong> <c:out value="${hotel.amenities != null ? hotel.amenities : 'N/A'}"/></p>
            <p><strong>Price per Night:</strong> £<fmt:formatNumber value="${hotel.pricePerNight}" minFractionDigits="2" maxFractionDigits="2"/></p>
            <a href="${pageContext.request.contextPath}/booking.jsp?hotelId=${hotel.id}" class="btn btn-primary">Book Now</a>
        </c:when>
        <c:otherwise>
            <p>Hotel not found. Please check the hotel ID or try again later.</p>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>