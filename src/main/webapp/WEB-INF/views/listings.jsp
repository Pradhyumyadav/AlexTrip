<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>AlexTrip - Hotel Listings in Leeds</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    /* Style definitions */
    :root { --primary-color: #FF5A5F; --primary-hover: #ff787c; --card-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }
    .hotel-listing { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 1.5rem; padding: 1rem; }
    .hotel-card { background-color: #fff; border-radius: 12px; box-shadow: var(--card-shadow); height: 100%; display: flex; flex-direction: column; }
    .hotel-img-wrapper { position: relative; padding-top: 56.25%; border-radius: 12px 12px 0 0; overflow: hidden; }
    .hotel-img { position: absolute; top: 0; left: 0; width: 100%; height: 100%; object-fit: cover; }
    .hotel-info { padding: 1.25rem; flex-grow: 1; display: flex; flex-direction: column; }
    .hotel-name { font-size: 1.25rem; font-weight: 600; color: #2d3436; }
    .hotel-location { color: #636e72; }
    .hotel-price { color: var(--primary-color); }
  </style>
</head>
<body>
<div class="container my-5">
  <header class="text-center mb-5">
    <h1 class="display-4 mb-3">Explore Stays in Leeds</h1>
  </header>
  <c:choose>
    <c:when test="${not empty hotels}">
      <div class="hotel-listing">
        <c:forEach var="hotel" items="${hotels}">
          <article class="hotel-card">
            <div class="hotel-img-wrapper">
              <img src="${fn:escapeXml(hotel.imageUrls[0])}" alt="View of ${fn:escapeXml(hotel.name)}" class="hotel-img" loading="lazy" />
            </div>
            <div class="hotel-info">
              <h2 class="hotel-name">${fn:escapeXml(hotel.name)}</h2>
              <p class="hotel-location">${fn:escapeXml(hotel.location)}</p>
              <p class="hotel-price">£${hotel.price}</p>
              <p><strong>Activity Type:</strong> ${fn:escapeXml(hotel.activityType)}</p>
              <a href="${pageContext.request.contextPath}/hotel/${hotel.id}" class="btn btn-primary btn-reserve">View Details</a>
            </div>
          </article>
        </c:forEach>
      </div>
    </c:when>
    <c:otherwise>
      <div class="no-hotels">No hotels found. Try adjusting your search criteria.</div>
    </c:otherwise>
  </c:choose>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>