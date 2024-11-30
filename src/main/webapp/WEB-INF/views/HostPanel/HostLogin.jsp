<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Host Login - AlexTrip</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <style>
        body, html {
            margin: 0;
            padding: 0;
            font-family: 'Roboto', sans-serif;
            background-color: #f7f7f7;
            height: 100%;
        }

        .login-wrapper {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .login-container {
            background: #ffffff;
            border-radius: 15px;
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
            padding: 30px;
            width: 400px;
            text-align: center;
        }

        .login-container h2 {
            font-weight: 700;
            color: #333333;
            margin-bottom: 20px;
        }

        .login-container .form-control {
            margin-bottom: 20px;
            height: 50px;
            font-size: 16px;
        }

        .btn-primary {
            background-color: #ff5a5f;
            border: none;
            font-size: 16px;
            font-weight: 600;
            width: 100%;
            height: 50px;
            border-radius: 8px;
        }

        .btn-primary:hover {
            background-color: #e04848;
        }

        .login-container .signup-link {
            margin-top: 15px;
            font-size: 14px;
        }

        .signup-link a {
            color: #ff5a5f;
            text-decoration: none;
            font-weight: 600;
        }

        .signup-link a:hover {
            text-decoration: underline;
        }

        .error-message {
            color: #d9534f;
            font-size: 14px;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<%@ include file="/WEB-INF/views/HostPanel/HostNavbar.jsp" %>

<div class="login-wrapper">
    <div class="login-container">
        <h2>Host Login</h2>
        <c:if test="${not empty errorMessage}">
            <p class="error-message">${errorMessage}</p>
        </c:if>
        <form action="<c:url value='/hostLogin'/>" method="post">
            <input type="email" name="email" class="form-control" placeholder="Email" required>
            <input type="password" name="password" class="form-control" placeholder="Password" required>
            <button type="submit" class="btn btn-primary">Log In</button>
        </form>
        <p class="signup-link">
            Don't have an account? <a href="<c:url value='/hostSignup'/>">Sign up here</a>
        </p>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>