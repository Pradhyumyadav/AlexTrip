<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AlexTrip - Discover Your Next Stay</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #FF385C;
            --primary-hover: #FF6B81;
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
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            width: 90%;
            max-width: 900px;
            margin: -5rem auto 2rem auto;
        }
        .btn-primary {
            background-color: var(--primary-color);
            border: none;
            padding: 0.75rem 1.5rem;
            font-size: 1.1rem;
            font-weight: bold;
        }
        .btn-primary:hover {
            background-color: var(--primary-hover);
        }
        .popular-section {
            padding: 2rem;
        }
        .footer {
            background-color: #f8f9fa;
            padding: 1rem;
            text-align: center;
        }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
    <div class="container">
        <a class="navbar-brand" href="#">AlexTrip</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-person-circle"></i> Account
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                        <li><a class="dropdown-item" href="<%= request.getContextPath() %>/login">Login</a></li>
                        <li><a class="dropdown-item" href="<%= request.getContextPath() %>/signup">Sign Up</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="<%= request.getContextPath() %>/admin">Admin Panel</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="hero">
    <div class="search-panel">
        <h1 class="mb-4 text-center">Discover Your Next Stay</h1>
        <form id="searchForm" action="<%= request.getContextPath() %>/listings" method="GET" class="row g-3">
            <div class="col-md-4">
                <label for="city" class="form-label">City</label>
                <input type="text" name="city" id="city" class="form-control" placeholder="Enter a city">
            </div>
            <div class="col-12 text-center mt-3">
                <button type="submit" class="btn btn-primary">Search Hotels</button>
            </div>
        </form>
    </div>
</div>

<div class="popular-section container">
    <h2 class="text-center mb-4" id="nearbyStaysHeader">Popular Stays Around You</h2>
    <div class="row" id="nearbyStays">
        <!-- Hotel cards will be dynamically injected here based on user location -->
    </div>
</div>

<div class="footer">
    <p>&copy; 2024 AlexTrip. All rights reserved.</p>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    async function fetchNearbyHotels() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                async (position) => {
                    const latitude = position.coords.latitude;
                    const longitude = position.coords.longitude;

                    const googleApiKey = "<%= System.getenv("GOOGLE_API_KEY") %>";
                    const geocodeUrl = `https://maps.googleapis.com/maps/api/geocode/json?latlng=${latitude},${longitude}&key=${googleApiKey}`;

                    try {
                        const response = await fetch(geocodeUrl);
                        const data = await response.json();

                        if (data.results && data.results.length > 0) {
                            const city = data.results[0].address_components.find(comp => comp.types.includes("locality")).long_name;
                            document.getElementById("nearbyStaysHeader").innerText = `Popular Stays Around ${city}`;
                            await fetchHotelsByCity(city);
                        }
                    } catch (error) {
                        console.error("Error fetching city name:", error);
                    }
                },
                (error) => {
                    console.error("Geolocation error:", error);
                    document.getElementById("nearbyStaysHeader").innerText = "Unable to determine your location.";
                }
            );
        } else {
            console.warn("Geolocation is not supported by this browser.");
            document.getElementById("nearbyStaysHeader").innerText = "Geolocation is not supported.";
        }
    }

    async function fetchHotelsByCity(city) {
        const encodedCity = encodeURIComponent(city);
        const url = `<%= request.getContextPath() %>/index?userCity=${encodedCity}`;

        try {
            const response = await fetch(url);
            const hotels = await response.json();

            const nearbyStaysContainer = document.getElementById("nearbyStays");
            nearbyStaysContainer.innerHTML = ""; // Clear existing content

            hotels.forEach(hotel => {
                const hotelCard = `
                    <div class="col-md-4 mb-4">
                        <div class="card shadow-sm">
                            <img src="${hotel.imageUrls[0] || 'default-image.jpg'}" class="card-img-top" alt="${hotel.name}">
                            <div class="card-body">
                                <h5 class="card-title">${hotel.name}</h5>
                                <p class="card-text">${hotel.location}</p>
                                <p class="card-text text-muted">£${hotel.price.toFixed(2)}</p>
                                <a href="<%= request.getContextPath() %>/hotelDetails?hotelId=${hotel.id}" class="btn btn-outline-primary w-100">View Details</a>
                            </div>
                        </div>
                    </div>
                `;
                nearbyStaysContainer.insertAdjacentHTML("beforeend", hotelCard);
            });
        } catch (error) {
            console.error("Error fetching hotels:", error);
        }
    }

    document.addEventListener("DOMContentLoaded", fetchNearbyHotels);
</script>
<script>
    const apiKey = "<c:out value='${GOOGLE_API_KEY}'/>"; // Use the passed API key

    async function fetchNearbyHotels() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                async (position) => {
                    const latitude = position.coords.latitude;
                    const longitude = position.coords.longitude;
                    const geocodeUrl = `https://maps.googleapis.com/maps/api/geocode/json?latlng=${latitude},${longitude}&key=${apiKey}`;

                    try {
                        const response = await fetch(geocodeUrl);
                        const data = await response.json();

                        if (data.results && data.results.length > 0) {
                            const city = data.results[0].address_components.find(comp => comp.types.includes("locality")).long_name;
                            document.getElementById("nearbyStaysHeader").innerText = `Popular Stays Around ${city}`;
                            await fetchHotelsByCity(city);
                        }
                    } catch (error) {
                        console.error("Error fetching city name:", error);
                    }
                },
                (error) => {
                    console.error("Geolocation error:", error);
                    document.getElementById("nearbyStaysHeader").innerText = "Unable to determine your location.";
                }
            );
        } else {
            console.warn("Geolocation is not supported by this browser.");
            document.getElementById("nearbyStaysHeader").innerText = "Geolocation is not supported.";
        }
    }
</script>

</body>
</html>