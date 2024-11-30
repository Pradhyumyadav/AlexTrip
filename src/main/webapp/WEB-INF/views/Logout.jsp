<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Logged Out - AlexTrip</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link href="${pageContext.request.contextPath}css/main.css" rel="stylesheet">
</head>
<body>
<div class="container text-center mt-5">
    <h1>You have been successfully logged out.</h1>
    <p class="lead">Thank you for using AlexTrip. We hope to see you again soon!</p>
    <a href="${pageContext.request.contextPath}/login" class="btn btn-primary mt-3">Login Again</a>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>