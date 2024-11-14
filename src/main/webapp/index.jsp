<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AlexTrip - Discover Your Next Stay</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet">
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light bg-light border-bottom shadow-sm mb-4">
    <div class="container">
        <a class="navbar-brand text-primary" href="#">AlexTrip</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin">Admin Panel</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/login">Login</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/signup">Sign Up</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="hero d-flex justify-content-center align-items-center py-5">
    <div class="search-panel bg-white p-4 rounded shadow-sm">
        <h1 class="text-center mb-4">Discover Your Next Stay</h1>
        <form action="${pageContext.request.contextPath}/listings" method="get" class="row g-3">
            <div class="col-md-3">
                <label for="city" class="form-label">City</label>
                <input type="text" id="city" name="city" class="form-control" placeholder="Enter a city" required autocomplete="off">
            </div>
            <div class="col-md-2">
                <label for="checkInDate" class="form-label">Check-in Date</label>
                <input type="date" id="checkInDate" name="checkIn" class="form-control" required>
            </div>
            <div class="col-md-2">
                <label for="checkOutDate" class="form-label">Check-out Date</label>
                <input type="date" id="checkOutDate" name="checkOut" class="form-control" required>
            </div>
            <div class="col-md-2">
                <label for="guests" class="form-label">Guests</label>
                <input type="number" id="guests" name="guests" class="form-control" placeholder="Guests" required min="1">
            </div>
            <div class="col-md-2">
                <label for="activityType" class="form-label">Activity Type</label>
                <select id="activityType" name="activityType" class="form-control">
                    <option value="">Activity Type</option>
                    <option value="leisure">Leisure</option>
                    <option value="business">Business</option>
                    <option value="adventure">Adventure</option>
                </select>
            </div>
            <div class="col-md-1">
                <button type="submit" class="btn btn-danger w-100">Search</button>
            </div>
        </form>
    </div>
</div>

<div class="container mt-5">
    <h2 class="text-center mb-4" id="nearbyStaysHeader">Stays Around You</h2>
    <div id="popularStays" class="row justify-content-center">
        <!-- Hotels based on geolocation will be inserted here via JavaScript -->
    </div>
</div>

<div class="footer mt-5 py-3 bg-light text-center border-top">
    <p>&copy; 2024 AlexTrip. All rights reserved.</p>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                fetchNearbyStays(position.coords.latitude, position.coords.longitude);
            }, function(error) {
                console.error("Geolocation error:", error);
                $('#nearbyStaysHeader').text('Unable to determine your location.');
            });
        } else {
            $('#nearbyStaysHeader').text('Geolocation is not supported by your browser.');
        }
    });

    function fetchNearbyStays(lat, lng) {
        $.ajax({
            url: '${pageContext.request.contextPath}/searchNearby',
            method: 'GET',
            data: { latitude: lat, longitude: lng },
            success: function(hotels) {
                if (hotels && hotels.length > 0) {
                    let hotelHtml = hotels.map(hotel => `
                        <div class="col-md-4 mb-4">
                            <div class="card shadow-sm">
                                <img src="${hotel.imageUrls[0] || 'default-image.jpg'}" class="card-img-top" alt="${hotel.name}">
                                <div class="card-body">
                                    <h5 class="card-title">${hotel.name}</h5>
                                    <p class="card-text">${hotel.description || 'No description available.'}</p>
                                    <p class="card-text"><strong>Rating:</strong> ${hotel.rating}</p>
                                    <p class="card-text"><strong>Price:</strong> ${hotel.price}</p>
                                    <a href="${pageContext.request.contextPath}/hotelDetails?hotelId=${hotel.id}" class="btn btn-primary">View Details</a>
                                </div>
                            </div>
                        </div>
                    `).join('');
                    $('#popularStays').html(hotelHtml);
                } else {
                    $('#popularStays').html('<p class="text-center">No nearby stays found.</p>');
                }
            },
            error: function() {
                $('#popularStays').html('<p class="text-center text-danger">Error loading nearby stays.</p>');
            }
        });
    }
</script>
</body>
</html>