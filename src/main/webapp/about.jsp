<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About Us - AlexTrip</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f8f9fa;
        }

        .navbar {
            background-color: #0056b3;
        }

        .navbar-brand, .navbar-nav .nav-link {
            color: #fff !important;
        }

        .navbar-brand:hover, .navbar-nav .nav-link:hover {
            color: #f8f9fa !important;
            text-decoration: underline;
        }

        .container {
            margin-top: 30px;
        }

        h1, h2 {
            color: #0056b3;
            font-weight: bold;
            margin-bottom: 20px;
        }

        section {
            background: #fff;
            padding: 20px;
            margin-bottom: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        footer {
            background-color: #0056b3;
            color: #fff;
            text-align: center;
            padding: 15px 0;
            margin-top: 30px;
        }

        .blockquote {
            border-left: 5px solid #0056b3;
            padding-left: 15px;
            font-style: italic;
        }
    </style>
</head>
<body>
<header>
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand" href="<c:url value='/index.jsp' />">AlexTrip</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/index.jsp' />">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="<c:url value='/about.jsp' />">About Us</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/contact.jsp' />">Contact</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/myTrips' />">My Trips</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</header>

<main class="container">
    <section>
        <h1>About Us</h1>
        <p>Welcome to <strong>AlexTrip</strong>, your premier destination for discovering and booking your next adventure!</p>
    </section>

    <section>
        <h2>Our Mission</h2>
        <p>At AlexTrip, we strive to provide unforgettable travel experiences tailored to your preferences, enabling you to explore the world with ease and comfort.</p>
    </section>

    <section>
        <h2>Our Team</h2>
        <p>Meet the team of passionate travel experts who make it all happen:</p>
        <ul class="list-group">
            <li class="list-group-item">John Doe - Founder & CEO</li>
            <li class="list-group-item">Jane Smith - Head of Operations</li>
            <li class="list-group-item">Emily White - Travel Coordinator</li>
        </ul>
    </section>

    <section>
        <h2>Testimonials</h2>
        <p>Don't just take our word for it - hear what our customers have to say!</p>
        <blockquote class="blockquote">
            "Thanks to AlexTrip, my trip to the Alps was nothing short of spectacular!" — Sarah Johnson
        </blockquote>
    </section>
</main>

<footer>
    <p>&copy; 2024 AlexTrip. All rights reserved.</p>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>