<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/HostPanel/HostNavbar.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Host Signup | AlexTrip</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <style>
        body, html {
            margin: 0;
            padding: 0;
            font-family: 'Roboto', sans-serif;
            background-color: #f7f7f7;
        }

        .signup-container {
            max-width: 500px;
            margin: 50px auto;
            padding: 30px;
            background: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        .signup-container h1 {
            font-weight: bold;
            font-size: 28px;
            text-align: center;
            margin-bottom: 20px;
            color: #333333;
        }

        .form-label {
            font-weight: 500;
            margin-bottom: 10px;
        }

        .btn-primary {
            background-color: #ff5a5f;
            border: none;
            font-weight: bold;
            font-size: 16px;
            padding: 12px 0;
            border-radius: 8px;
        }

        .btn-primary:hover {
            background-color: #e0474b;
        }

        .footer {
            text-align: center;
            padding: 20px 0;
            background: #333333;
            color: #ffffff;
            margin-top: 50px;
        }

        .footer p {
            margin: 0;
            font-size: 14px;
        }

        .alert {
            font-size: 14px;
            margin-top: 15px;
        }
    </style>
</head>
<body>

<div class="signup-container">
    <h1>Become a Host</h1>
    <p class="text-center text-muted mb-4">Join AlexTrip and start sharing your unique stays and adventures.</p>

    <form method="post" action="<c:url value='/hostSignup'/>">
        <div class="mb-3">
            <label for="name" class="form-label">Name</label>
            <input type="text" id="name" name="name" class="form-control" placeholder="Enter your name" required>
        </div>
        <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input type="email" id="email" name="email" class="form-control" placeholder="Enter your email address" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input type="password" id="password" name="password" class="form-control" placeholder="Create a password" required>
        </div>
        <button type="submit" class="btn btn-primary w-100">Sign Up</button>
        <div class="text-center mt-3">
            Already have an account? <a href="<c:url value='/hostLogin'/>" class="text-primary">Log in</a>
        </div>
    </form>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger mt-3" role="alert">${errorMessage}</div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success mt-3" role="alert">${successMessage}</div>
    </c:if>
</div>

<div class="footer">
    <p>&copy; 2024 AlexTrip. All rights reserved.</p>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>