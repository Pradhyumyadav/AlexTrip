<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - AlexTrip</title>
    <!-- Link to external CSS files -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/WEB-INF/views/css/styles.css">
</head>
<body>
<div class="error-container">
    <h1 class="error-title">Oops! Something went wrong.</h1>
    <p class="error-message">
        <c:choose>
            <c:when test="${not empty sessionScope.errorMessage}">
                ${sessionScope.errorMessage}
            </c:when>
            <c:otherwise>
                An unexpected error occurred. Please try again later.
            </c:otherwise>
        </c:choose>
    </p>
    <a href="<%= request.getContextPath() %>/" class="btn-primary">Go Back to Homepage</a>
</div>
</body>
</html>