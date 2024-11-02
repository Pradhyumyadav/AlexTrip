<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AlexTrip - Discover Your Next Stay</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        /* Add any styling as needed */
    </style>
</head>
<body class="bg-light">

<div class="container mt-5">
    <!-- Search Section -->
    <div class="search-container">
        <div class="search-title text-center mb-4">Discover Your Next Stay</div>
        <form action="${pageContext.request.contextPath}/hotels" method="GET" class="row g-3">
            <div class="col-md-4">
                <label for="location" class="form-label">Location</label>
                <input type="text" name="location" id="location" class="form-control" placeholder="Where are you going?"
                       value="${param.location != null ? param.location : ''}">
            </div>
            <div class="col-md-3">
                <label for="checkIn" class="form-label">Check-in</label>
                <input type="date" name="checkIn" id="checkIn" class="form-control" placeholder="Check-in"
                       value="${param.checkIn != null ? param.checkIn : ''}">
            </div>
            <div class="col-md-3">
                <label for="checkOut" class="form-label">Check-out</label>
                <input type="date" name="checkOut" id="checkOut" class="form-control" placeholder="Check-out"
                       value="${param.checkOut != null ? param.checkOut : ''}">
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary w-100">Search</button>
            </div>
        </form>
    </div>

    <!-- Popular Destinations Around You Section -->
    <section class="mt-5">
        <h2 class="text-center mb-4" id="popular-destinations-label">Popular Destinations Around You</h2>
        <div class="row" id="popular-destinations-container">
            <c:forEach var="destination" items="${popularDestinations}">
                <div class="col-md-6 col-lg-4 mb-4">
                    <div class="card destination-card">
                        <img src="${fn:escapeXml(destination.imageUrl != null && fn:length(destination.imageUrl) > 0 ? destination.imageUrl : pageContext.request.contextPath + '/images/placeholder.jpg')}"
                             alt="${fn:escapeXml(destination.name != null ? destination.name : 'No Image Available')}"
                             class="card-img-top destination-img">
                        <div class="card-body">
                            <h5 class="destination-name">${fn:escapeXml(destination.name != null ? destination.name : 'Unknown Destination')}</h5>
                            <p class="destination-desc">${fn:escapeXml(destination.description != null ? destination.description : 'No description available.')}</p>
                            <a href="${pageContext.request.contextPath}/hotels?location=${fn:escapeXml(destination.name != null ? destination.name : '')}"
                               class="btn btn-outline-primary btn-sm">
                                Explore ${fn:escapeXml(destination.name != null ? destination.name : 'Destination')}
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </section>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const checkInInput = document.querySelector('input[name="checkIn"]');
        const checkOutInput = document.querySelector('input[name="checkOut"]');

        // Set minimum date for check-in to today
        checkInInput.min = new Date().toISOString().split('T')[0];

        checkInInput.addEventListener('change', function() {
            // Set minimum check-out date based on check-in date
            checkOutInput.min = this.value;
            if (checkOutInput.value && checkOutInput.value < this.value) {
                checkOutInput.value = this.value;
            }
        });
    });
</script>
</body>
</html>