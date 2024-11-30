<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/HostPanel/HostNavbar.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Host Management Panel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(to right, #6a11cb, #2575fc);
            color: #fff;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: flex-start;
            padding: 20px;
        }
        .container {
            background: rgba(255, 255, 255, 0.95);
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            max-width: 1200px;
            margin-top: 20px;
        }
        .btn { margin: 5px; }
        .hidden-form {
            display: none;
            margin-top: 10px;
        }
        label {
            display: block;
            margin-top: 5px;
        }
        textarea { width: 100%; resize: none; }
        img.trip-photo {
            max-width: 100px;
            max-height: 100px;
            margin: 5px;
        }
        .section-title {
            color: #6a11cb;
            margin-top: 20px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="container">
    <h2 class="text-center">Host Management Panel</h2>
    <hr>

    <!-- Manage Trips Section -->
    <h4 class="section-title">Manage Trips</h4>
    <table class="table table-dark table-hover">
        <thead>
        <tr>
            <th>Trip Name</th>
            <th>Destination</th>
            <th>Start Date</th>
            <th>End Date</th>
            <th>Duration</th>
            <th>Max Participants</th>
            <th>Price</th>
            <th>Photos</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${trips}" var="trip">
            <tr>
                <td>${trip.tripName}</td>
                <td>${trip.destination}</td>
                <td>${trip.startDate}</td>
                <td>${trip.endDate}</td>
                <td>${trip.duration} days</td>
                <td>${trip.maxParticipants}</td>
                <td>&pound;${trip.price}</td>
                <td>
                    <c:forEach items="${trip.photoPaths}" var="photo">
                        <img src="${photo}" class="trip-photo" alt="Trip Photo">
                    </c:forEach>
                </td>
                <td>
                    <button class="btn btn-primary btn-sm" onclick="toggleForm('editTripForm-${trip.tripId}')">Edit</button>
                    <a href="HostPanel?action=deleteTrip&trip_id=${trip.tripId}" class="btn btn-danger btn-sm">Delete</a>
                </td>
            </tr>
            <tr id="editTripForm-${trip.tripId}" class="hidden-form">
                <td colspan="9">
                    <form action="HostPanel" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="updateTrip">
                        <input type="hidden" name="trip_id" value="${trip.tripId}">
                        <label>Trip Name: <input type="text" name="trip_name" value="${trip.tripName}" required></label>
                        <label>Destination: <input type="text" name="destination" value="${trip.destination}" required></label>
                        <label>Start Date: <input type="date" name="start_date" value="${trip.startDate}" required></label>
                        <label>End Date: <input type="date" name="end_date" value="${trip.endDate}" required></label>
                        <label>Duration (Days): <input type="number" name="duration" value="${trip.duration}" required></label>
                        <label>Max Participants: <input type="number" name="max_participants" value="${trip.maxParticipants}" required></label>
                        <label>Price (&pound;): <input type="number" step="0.01" name="price" value="${trip.price}" required></label>
                        <label>Description: <textarea name="description">${trip.description}</textarea></label>
                        <label>Cancellation Policy: <textarea name="cancellation_policy">${trip.cancellationPolicy}</textarea></label>
                        <label>Inclusions: <textarea name="inclusions">${trip.inclusions}</textarea></label>
                        <label>Exclusions: <textarea name="exclusions">${trip.exclusions}</textarea></label>
                        <label>Upload Photos: <input type="file" name="photos" multiple></label>
                        <input type="submit" class="btn btn-success" value="Update Trip">
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <button class="btn btn-success" onclick="toggleForm('addTripForm')">Add New Trip</button>
    <div id="addTripForm" class="hidden-form">
        <form action="HostPanel" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="addTrip">
            <label>Trip Name: <input type="text" name="trip_name" placeholder="Trip Name" required></label>
            <label>Destination: <input type="text" name="destination" placeholder="Destination" required></label>
            <label>Start Date: <input type="date" name="start_date" required></label>
            <label>End Date: <input type="date" name="end_date" required></label>
            <label>Duration (Days): <input type="number" name="duration" placeholder="Duration" required></label>
            <label>Max Participants: <input type="number" name="max_participants" placeholder="Max Participants" required></label>
            <label>Price (&pound;): <input type="number" step="0.01" name="price" placeholder="Price" required></label>
            <label>Description: <textarea name="description" placeholder="Trip Description"></textarea></label>
            <label>Cancellation Policy: <textarea name="cancellation_policy" placeholder="Cancellation Policy"></textarea></label>
            <label>Inclusions: <textarea name="inclusions" placeholder="Trip Inclusions"></textarea></label>
            <label>Exclusions: <textarea name="exclusions" placeholder="Trip Exclusions"></textarea></label>
            <label>Upload Photos: <input type="file" name="photos" multiple></label>
            <input type="submit" class="btn btn-primary" value="Add Trip">
        </form>
    </div>
    <hr>

    <!-- Manage Offers Section -->
    <h4 class="section-title">Manage Offers</h4>
    <table class="table table-dark table-hover">
        <thead>
        <tr>
            <th>Details</th>
            <th>Discounted Price</th>
            <th>Active</th>
            <th>Trip</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${offers}" var="offer">
            <tr>
                <td>${offer.details}</td>
                <td>&pound;${offer.discountedPrice}</td>
                <td>${offer.active ? 'Yes' : 'No'}</td>
                <td>${offer.tripName}</td>
                <td>
                    <button class="btn btn-primary btn-sm" onclick="toggleForm('editOfferForm-${offer.id}')">Edit</button>
                    <a href="HostPanel?action=deleteOffer&offer_id=${offer.id}" class="btn btn-danger btn-sm">Delete</a>
                </td>
            </tr>
            <tr id="editOfferForm-${offer.id}" class="hidden-form">
                <td colspan="5">
                    <form action="HostPanel" method="post">
                        <input type="hidden" name="action" value="updateOffer">
                        <input type="hidden" name="offer_id" value="${offer.id}">
                        <label>Details: <input type="text" name="details" value="${offer.details}" required></label>
                        <label>Discounted Price (&pound;): <input type="number" step="0.01" name="discounted_price" value="${offer.discountedPrice}" required></label>
                        <label>Active:
                            <select name="is_active">
                                <option value="true" ${offer.active ? 'selected' : ''}>Yes</option>
                                <option value="false" ${!offer.active ? 'selected' : ''}>No</option>
                            </select>
                        </label>
                        <label>Trip:
                            <select name="trip_id">
                                <c:forEach items="${trips}" var="trip">
                                    <option value="${trip.tripId}" ${trip.tripId == offer.tripId ? 'selected' : ''}>${trip.tripName}</option>
                                </c:forEach>
                            </select>
                        </label>
                        <input type="submit" class="btn btn-success" value="Update Offer">
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <button class="btn btn-success" onclick="toggleForm('addOfferForm')">Add New Offer</button>
    <div id="addOfferForm" class="hidden-form">
        <form action="HostPanel" method="post">
            <input type="hidden" name="action" value="addOffer">
            <label>Details: <input type="text" name="details" placeholder="Details" required></label>
            <label>Discounted Price (&pound;): <input type="number" step="0.01" name="discounted_price" placeholder="Discounted Price" required></label>
            <label>Active:
                <select name="is_active">
                    <option value="true">Yes</option>
                    <option value="false">No</option>
                </select>
            </label>
            <label>Trip:
                <select name="trip_id">
                    <c:forEach items="${trips}" var="trip">
                        <option value="${trip.tripId}">${trip.tripName}</option>
                    </c:forEach>
                </select>
            </label>
            <input type="submit" class="btn btn-primary" value="Add Offer">
        </form>
    </div>
</div>
<script>
    function toggleForm(formId) {
        const form = document.getElementById(formId);
        form.style.display = form.style.display === "none" ? "block" : "none";
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>