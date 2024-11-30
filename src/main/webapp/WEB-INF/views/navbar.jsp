<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-expand-lg navbar-dark" style="background: linear-gradient(to right, #6a11cb, #2575fc);">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/index" style="font-weight: bold; font-size: 24px;">AlexTrip</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link text-light" href="${pageContext.request.contextPath}/about.jsp" style="font-size: 16px;">About</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-light" href="${pageContext.request.contextPath}/contact.jsp" style="font-size: 16px;">Contact</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-light" href="${pageContext.request.contextPath}/myTrips" style="font-size: 16px;">My Trips</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-light" href="${pageContext.request.contextPath}/logout" style="font-size: 16px;">Logout</a>
                </li>
            </ul>
        </div>
    </div>
</nav>