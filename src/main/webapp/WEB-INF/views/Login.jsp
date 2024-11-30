<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AlexTrip - Log in</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <style>
        body, html {
            height: 100%;
            margin: 0;
            padding: 0;
            overflow-x: hidden;
            font-family: 'Roboto', sans-serif;
        }

        .carousel, .carousel-item, .carousel-item img {
            height: 100vh;
            width: 100vw;
            object-fit: cover;
        }

        .login-form-container {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 100%;
            max-width: 350px;
            padding: 15px;
            background: rgba(255, 255, 255, 0.95);
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.5);
        }

        .form-control {
            height: 50px;
            margin-bottom: 15px;
        }

        .btn-primary {
            width: 100%;
            font-weight: bold;
        }

        .carousel-indicators [data-bs-target] {
            background-color: #fff;
        }
    </style>
</head>
<body>
<%@ include file="/WEB-INF/views/navbar.jsp" %>

<!-- Bootstrap Carousel for Background -->
<div id="loginPageCarousel" class="carousel slide" data-bs-ride="carousel" data-bs-interval="4000">
    <div class="carousel-inner">
        <c:forEach items="${imagePaths}" var="imagePath" varStatus="status">
            <div class="carousel-item ${status.first ? 'active' : ''}">
                <img src="${pageContext.request.contextPath}/${imagePath}" class="d-block w-100" alt="Slide">
            </div>
        </c:forEach>
    </div>
    <button class="carousel-control-prev" type="button" data-bs-target="#loginPageCarousel" data-bs-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
    </button>
    <button class="carousel-control-next" type="button" data-bs-target="#loginPageCarousel" data-bs-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
    </button>
</div>

<div class="login-form-container">
    <h3 class="text-center mb-3">Log in to AlexTrip</h3>
    <form action="${pageContext.request.contextPath}/login" method="post">
        <label>
            <input type="email" name="email" class="form-control" placeholder="Email address" required>
        </label>
        <label>
            <input type="password" name="password" class="form-control" placeholder="Password" required>
        </label>
        <button type="submit" class="btn btn-primary mt-3">Log In</button>
        <div class="text-center mt-2">
            Don't have an account? <a href="${pageContext.request.contextPath}/signup">Sign up</a>
        </div>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>