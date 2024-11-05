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
            height: 100%;
            margin: 0;
            font-family: Arial, sans-serif;
        }
        .hero {
            background: url('https://source.unsplash.com/1600x900/?travel,cityscape') no-repeat center center/cover;
            height: 100vh;
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
            width: 100%;
            max-width: 800px;
        }
        .form-control, .form-select, .btn-primary {
            border-radius: 20px;
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
        .form-label {
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="hero">
    <div class="search-panel">
        <h1 class="mb-4 text-center">Discover Your Next Stay</h1>
        <form id="searchForm" action="<%= request.getContextPath() %>/listings" method="GET" class="row g-3">
            <div class="col-md-4">
                <label for="city" class="form-label">City</label>
                <input type="text" name="city" id="city" class="form-control" placeholder="Enter a city"
                       value="${param.city != null ? param.city : ''}" required>
            </div>
            <div class="col-md-4">
                <label for="activityType" class="form-label">Activity Type</label>
                <select name="activityType" id="activityType" class="form-select">
                    <option value="">Any</option>
                    <c:forEach var="type" items="${activityTypes != null ? activityTypes : []}">
                        <option value="${type}" ${param.activityType == type ? 'selected' : ''}>${type}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-4">
                <label for="duration" class="form-label">Duration (Days)</label>
                <select name="duration" id="duration" class="form-select">
                    <option value="">Any</option>
                    <option value="1-3" ${param.duration == '1-3' ? 'selected' : ''}>1-3 days</option>
                    <option value="4-7" ${param.duration == '4-7' ? 'selected' : ''}>4-7 days</option>
                    <option value="8-14" ${param.duration == '8-14' ? 'selected' : ''}>8-14 days</option>
                    <option value="15+" ${param.duration == '15+' ? 'selected' : ''}>15+ days</option>
                </select>
            </div>
            <div class="col-md-4">
                <label for="priceRange" class="form-label">Price Range (£)</label>
                <select name="priceRange" id="priceRange" class="form-select">
                    <option value="">Any</option>
                    <option value="0-100" ${param.priceRange == '0-100' ? 'selected' : ''}>£0 - £100</option>
                    <option value="100-500" ${param.priceRange == '100-500' ? 'selected' : ''}>£100 - £500</option>
                    <option value="500-1000" ${param.priceRange == '500-1000' ? 'selected' : ''}>£500 - £1000</option>
                    <option value="1000+" ${param.priceRange == '1000+' ? 'selected' : ''}>£1000+</option>
                </select>
            </div>
            <div class="col-md-4">
                <label for="radius" class="form-label">Search Radius (km)</label>
                <input type="number" name="radius" id="radius" class="form-control" placeholder="Enter radius"
                       value="${param.radius != null ? param.radius : ''}" min="1" required>
            </div>
            <div class="col-md-4">
                <label for="sortPrice" class="form-label">Sort by Price</label>
                <select name="sortPrice" id="sortPrice" class="form-select">
                    <option value="">Select</option>
                    <option value="low-high" ${param.sortPrice == 'low-high' ? 'selected' : ''}>Low to High</option>
                    <option value="high-low" ${param.sortPrice == 'high-low' ? 'selected' : ''}>High to Low</option>
                </select>
            </div>
            <div class="col-12 text-center mt-3">
                <button type="submit" class="btn btn-primary">Search Hotels</button>
            </div>
        </form>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger mt-3" role="alert">
                    ${param.error}
            </div>
        </c:if>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>