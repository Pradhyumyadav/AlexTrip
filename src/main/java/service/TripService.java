package service;

import Model.Trip;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripService {

    private final DataSource dataSource;

    public TripService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Retrieve all trips for a specific host
    public List<Trip> getAllTripsForHost(int hostId) {
        List<Trip> trips = new ArrayList<>();
        String sql = "SELECT * FROM trips WHERE host_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hostId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                trips.add(createTripFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getAllTripsForHost: " + e.getMessage());
        }
        return trips;
    }

    // Retrieve all trips for public view
    public List<Trip> getAllTrips() {
        List<Trip> trips = new ArrayList<>();
        String sql = "SELECT * FROM trips";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                trips.add(createTripFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getAllTrips: " + e.getMessage());
        }
        return trips;
    }

    // Save a new or existing trip
    public void saveTrip(Trip trip) {
        if (trip.getTripId() == 0) {
            addTrip(trip);
        } else {
            updateTrip(trip);
        }
    }

    // Add a new trip
    private void addTrip(Trip trip) {
        String sql = "INSERT INTO trips (trip_name, destination, duration, price, activity_type, description, start_date, end_date, max_participants, photos, cancellation_policy, itinerary, inclusions, exclusions, difficulty_level, packing_list, booking_deadline, payment_terms, host_name, host_contact_email, host_contact_phone, host_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setTripParameters(stmt, trip);
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trip.setTripId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in addTrip: " + e.getMessage());
        }
    }

    // Update an existing trip
    private void updateTrip(Trip trip) {
        String sql = "UPDATE trips SET trip_name = ?, destination = ?, duration = ?, price = ?, activity_type = ?, description = ?, start_date = ?, end_date = ?, max_participants = ?, photos = ?, cancellation_policy = ?, itinerary = ?, inclusions = ?, exclusions = ?, difficulty_level = ?, packing_list = ?, booking_deadline = ?, payment_terms = ?, host_name = ?, host_contact_email = ?, host_contact_phone = ? WHERE trip_id = ? AND host_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setTripParameters(stmt, trip);
            stmt.setInt(22, trip.getTripId());
            stmt.setInt(23, trip.getHostId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL Error in updateTrip: " + e.getMessage());
        }
    }

    // Delete a trip
    public void deleteTrip(int tripId, int hostId) {
        String sql = "DELETE FROM trips WHERE trip_id = ? AND host_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tripId);
            stmt.setInt(2, hostId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL Error in deleteTrip: " + e.getMessage());
        }
    }

    // Retrieve trip details by trip ID
    public Trip getTripById(int tripId) {
        String sql = "SELECT * FROM trips WHERE trip_id = ?";
        Trip trip = null;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tripId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    trip = createTripFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getTripById: " + e.getMessage());
        }
        return trip;
    }

    // Retrieve all distinct activity types
    public List<String> getDistinctActivityTypes() {
        List<String> types = new ArrayList<>();
        String sql = "SELECT DISTINCT activity_type FROM trips";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                types.add(rs.getString("activity_type"));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getDistinctActivityTypes: " + e.getMessage());
        }
        return types;
    }

    // Search trips by filters
    public List<Trip> searchTrips(String destination, Integer duration, BigDecimal maxPrice, String activityType) {
        List<Trip> trips = new ArrayList<>();
        String sql = "SELECT * FROM trips WHERE destination ILIKE ? " +
                "AND (duration <= ? OR ? IS NULL) " +
                "AND (price <= ? OR ? IS NULL) " +
                "AND (activity_type = ? OR ? IS NULL)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Set destination
            stmt.setString(1, "%" + destination + "%");

            // Set duration
            if (duration != null) {
                stmt.setInt(2, duration); // duration <= ?
                stmt.setInt(3, duration); // ? IS NULL
            } else {
                stmt.setNull(2, Types.INTEGER);
                stmt.setNull(3, Types.INTEGER);
            }

            // Set maxPrice
            if (maxPrice != null) {
                stmt.setBigDecimal(4, maxPrice); // price <= ?
                stmt.setBigDecimal(5, maxPrice); // ? IS NULL
            } else {
                stmt.setNull(4, Types.NUMERIC);
                stmt.setNull(5, Types.NUMERIC);
            }

            // Set activityType
            if (activityType != null && !activityType.isEmpty()) {
                stmt.setString(6, activityType); // activity_type = ?
                stmt.setString(7, activityType); // ? IS NULL
            } else {
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.VARCHAR);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    trips.add(createTripFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in searchTrips: " + e.getMessage());
        }
        return trips;
    }

    // Helper method to set parameters for prepared statements
    private void setTripParameters(PreparedStatement stmt, Trip trip) throws SQLException {
        stmt.setString(1, trip.getTripName());
        stmt.setString(2, trip.getDestination());
        stmt.setInt(3, trip.getDuration());
        stmt.setBigDecimal(4, trip.getPrice());
        stmt.setString(5, trip.getActivityType());
        stmt.setString(6, trip.getDescription());
        stmt.setObject(7, trip.getStartDate() != null ? Date.valueOf(trip.getStartDate()) : null);
        stmt.setObject(8, trip.getEndDate() != null ? Date.valueOf(trip.getEndDate()) : null);
        stmt.setInt(9, trip.getMaxParticipants());
        stmt.setString(10, Trip.fromListToCommaSeparatedString(trip.getPhotoPaths()));
        stmt.setString(11, trip.getCancellationPolicy());
        stmt.setString(12, trip.getItinerary());
        stmt.setString(13, trip.getInclusions());
        stmt.setString(14, trip.getExclusions());
        stmt.setString(15, trip.getDifficultyLevel());
        stmt.setString(16, trip.getPackingList());
        stmt.setObject(17, trip.getBookingDeadline() != null ? Date.valueOf(trip.getBookingDeadline()) : null);
        stmt.setString(18, trip.getPaymentTerms());
        stmt.setString(19, trip.getHostName());
        stmt.setString(20, trip.getHostContactEmail());
        stmt.setString(21, trip.getHostContactPhone());
    }

    // Helper method to create a Trip object from ResultSet
    private Trip createTripFromResultSet(ResultSet rs) throws SQLException {
        return new Trip(
                rs.getInt("trip_id"),
                rs.getString("trip_name"),
                rs.getString("destination"),
                rs.getInt("duration"),
                rs.getBigDecimal("price"),
                rs.getString("activity_type"),
                rs.getString("description"),
                rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null,
                rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null,
                rs.getInt("max_participants"),
                Trip.fromCommaSeparatedStringToList(rs.getString("photos")),
                rs.getInt("host_id"),
                rs.getString("cancellation_policy"),
                rs.getString("itinerary"),
                rs.getString("inclusions"),
                rs.getString("exclusions"),
                rs.getString("difficulty_level"),
                rs.getString("packing_list"),
                rs.getDate("booking_deadline") != null ? rs.getDate("booking_deadline").toLocalDate() : null,
                rs.getString("payment_terms"),
                rs.getString("host_name"),
                rs.getString("host_contact_email"),
                rs.getString("host_contact_phone")
        );
    }
}