<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>AlexTrip - Hotel Listings</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    :root {
      --primary-color: #FF5A5F;
      --primary-hover: #ff787c;
      --card-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }
    body {
      font-family: Arial, sans-serif;
    }
    .hotel-listing {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 1.5rem;
      padding: 1rem;
    }
    .hotel-card {
      background-color: #fff;
      border-radius: 12px;
      box-shadow: var(--card-shadow);
      height: 100%;
      display: flex;
      flex-direction: column;
      cursor: pointer;
      transition: transform 0.3s, box-shadow 0.3s;
      text-decoration: none;
      color: inherit;
    }
    .hotel-card:hover {
      transform: scale(1.03);
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
    }
    .hotel-img-wrapper {
      position: relative;
      padding-top: 56.25%;
      border-radius: 12px 12px 0 0;
      overflow: hidden;
      background-color: #f0f0f0;
      color: #aaa;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 1.2rem;
    }
    .hotel-img {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
    .hotel-info {
      padding: 1.25rem;
      flex-grow: 1;
      display: flex;
      flex-direction: column;
    }
    .hotel-name {
      font-size: 1.25rem;
      font-weight: 600;
      color: #2d3436;
    }
    .hotel-location, .hotel-price {
      color: #636e72;
    }
    .hotel-price {
      color: var(--primary-color);
    }
  </style>
</head>
<body>

<div class="container my-5">
  <header class="text-center mb-5">
    <h1 class="display-4 mb-3">Explore Stays in <c:out value="${param.city}"/></h1>
  </header>

  <c:choose>
    <c:when test="${not empty hotels}">
      <div class="hotel-listing">
        <jsp:useBean id="hotels" scope="request" type="java.util.List"/>
        <c:forEach var="hotel" items="${hotels}">
          <a href="${pageContext.request.contextPath}/hotel/details?hotelId=${hotel.place_id}" class="hotel-card">
            <div class="hotel-img-wrapper">
              <c:choose>
                <c:when test="${not empty hotel.photos}">
                  <c:if test="${not empty hotel.photos[0].photo_reference}">
                    <img src="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${hotel.photos[0].photo_reference}&key=YOUR_GOOGLE_API_KEY" alt="View of ${fn:escapeXml(hotel.name)}" class="hotel-img" loading="lazy">
                  </c:if>
                </c:when>
                <c:otherwise>
                  <span>No Image Available</span>
                </c:otherwise>
              </c:choose>
            </div>
            <div class="hotel-info">
              <h2 class="hotel-name">${fn:escapeXml(hotel.name)}</h2>
              <p class="hotel-location">${fn:escapeXml(hotel.formatted_address)}</p>
              <p class="hotel-price">£<fmt:formatNumber value="${hotel.price}" minFractionDigits="2" maxFractionDigits="2"/></p>
              <p><strong>Activity Type:</strong> <c:out value="${hotel.activityType != null && !hotel.activityType.isEmpty() ? hotel.activityType : 'Not specified'}"/></p>
            </div>
          </a>
        </c:forEach>
      </div>
    </c:when>
    <c:otherwise>
      <div class="text-center">
        <h4>No hotels found. Try adjusting your search criteria.</h4>
        <p>Check back later for new listings!</p>
      </div>
    </c:otherwise>
  </c:choose>
</div>

<footer class="text-center my-4">
  <p>&copy; 2024 AlexTrip - All rights reserved.</p>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>