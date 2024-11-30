<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Welcome to AlexTrip</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
  <style>
    body, html {
      margin: 0;
      padding: 0;
      font-family: 'Roboto', sans-serif;
      height: 100vh;
      background: linear-gradient(to right, #ff5a5f, #008489);
    }

    .welcome-wrapper {
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100%;
      text-align: center;
    }

    .card-container {
      display: flex;
      gap: 30px;
      flex-wrap: wrap;
      justify-content: center;
    }

    .card {
      background: rgba(255, 255, 255, 0.8);
      padding: 20px;
      border-radius: 15px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
      width: 250px;
      height: 200px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
    }

    .card:hover {
      transform: scale(1.1);
      box-shadow: 0 6px 18px rgba(0, 0, 0, 0.4);
    }

    .card h3 {
      font-size: 1.5rem;
      font-weight: 700;
      color: #333333;
      margin-bottom: 15px;
    }

    .card a {
      text-decoration: none;
      color: #ffffff;
      font-size: 16px;
      font-weight: bold;
      padding: 10px 20px;
      background: #ff5a5f;
      border-radius: 5px;
      transition: background 0.2s ease-in-out;
    }

    .card a:hover {
      background: #e04848;
    }

    .welcome-header {
      color: #ffffff;
      margin-bottom: 30px;
      font-size: 2.5rem;
      font-weight: 700;
    }
  </style>
</head>
<body>
<div class="welcome-wrapper">
  <div>
    <h1 class="welcome-header">Welcome to AlexTrip</h1>
    <div class="card-container">
      <!-- User Login Card -->
      <div class="card">
        <h3>User Login</h3>
        <p>Plan your next trip and book amazing destinations.</p>
        <a href="<c:url value='/login'/>">Log in as User</a>
      </div>
      <!-- Host Login Card -->
      <div class="card">
        <h3>Host Login</h3>
        <p>Manage your listings and connect with travelers.</p>
        <a href="<c:url value='/hostLogin'/>">Log in as Host</a>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>