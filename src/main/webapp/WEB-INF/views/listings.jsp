<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <!-- Head content remains the same -->
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>AlexTrip - Discover Your Next Stay</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    /* Your CSS styles here */
    :root {
      --primary-color: #FF5A5F;
      --primary-hover: #ff787c;
      --card-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }
    body, html {
      margin: 0;
      font-family: Arial, sans-serif;
    }
    .navbar-brand {
      font-weight: bold;
    }
    .hero {
      background: url('https://source.unsplash.com/1600x900/?travel,cityscape') no-repeat center center/cover;
      height: 60vh;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
    }
    .search-panel {
      background: rgba(255, 255, 255, 0.9);
      padding: 2rem;
      border-radius: 12px;
      box-shadow: var(--card-shadow);
      width: 100%;
      max-width: 800px;
      margin: -3rem auto 2rem auto;
    }
    .popular-section {
      padding: 2rem;
    }
    .hotel-listing {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 1.5rem;
    }
    .hotel-card {
      background-color: #fff;
      border-radius: 12px;
      box-shadow: var(--card-shadow);
      text-decoration: none;
      color: inherit;
      transition: transform 0.3s, box-shadow 0.3s;
    }
    .hotel-card:hover {
      transform: scale(1.03);
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
    }
    .hotel-img {
      width: 100%;
      height: 200px;
      object-fit: cover;
      border-radius: 12px 12px 0 0;
    }
    .error-message {
      text-align: center;
      color: red;
      font-weight: bold;
      margin-top: 2rem;
    }
  </style>
</head>
<body>

<!-- Header with Navbar -->
<nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
  <!-- Navbar content remains the same -->
  <div class="container">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/">AlexTrip</a>
    <!-- Rest of your navbar code -->
  </div>
</nav>

<!-- Hero Section with Search Panel -->
<div class="hero">
  <div class="search-panel">
    <h1 class="mb-4 text-center">Discover Your Next Stay</h1>
    <form id="searchForm" action="${pageContext.request.contextPath}/search" method="GET" class="row g-3">
      <div class="col-md-4">
        <label for="city" class="form-label">City</label>
        <input type="text" name="city" id="city" class="form-control" placeholder="Enter a city" value="${param.city}" required>
      </div>
      <!-- Add other search parameters if needed -->
      <div class="col-12 text-center mt-3">
        <button type="submit" class="btn btn-primary">Search Hotels</button>
      </div>
    </form>
  </div>
</div>

<!-- Hotel Listings Section -->
<div class="container popular-section">
  <c:choose>
    <c:when test="${not empty hotels}">
      <h2 class="text-center mb-4">Search Results</h2>
      <div class="hotel-listing">
        <c:forEach var="hotel" items="${hotels}">
          <a href="${pageContext.request.contextPath}/hotelDetails?hotelId=${hotel.id}" class="hotel-card">
            <c:choose>
              <c:when test="${not empty hotel.imageUrls}">
                <img src="${hotel.imageUrls[0]}" class="hotel-img" alt="${fn:escapeXml(hotel.name)}">
              </c:when>
              <c:otherwise>
                <img src="default-image.jpg" class="hotel-img" alt="${fn:escapeXml(hotel.name)}">
              </c:otherwise>
            </c:choose>
            <div class="hotel-info p-3">
              <h5 class="hotel-name">${fn:escapeXml(hotel.name)}</h5>
              <p class="hotel-location">${fn:escapeXml(hotel.location)}</p>
              <p class="hotel-price">£<fmt:formatNumber value="${hotel.price}" minFractionDigits="2" maxFractionDigits="2"/></p>
            </div>
          </a>
        </c:forEach>
      </div>
    </c:when>
    <c:otherwise>
      <!-- Display popular stays using JavaScript if no search results -->
      <h2 class="text-center mb-4" id="popularStaysHeader">Popular Stays</h2>
      <div id="errorMessage" class="error-message"></div>
      <div class="hotel-listing" id="popularStays">
        <!-- Hotel cards will be dynamically injected here -->
      </div>
    </c:otherwise>
  </c:choose>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<c:if test="${empty hotels}">
  <script>
    // JavaScript code for fetching popular stays
    // [Your existing JavaScript code for fetching hotels based on geolocation]
    // Remember to escape the $ characters in JavaScript template literals
  </script>
</c:if>

</body>
</html>