package service;

import Model.Offer;
import utils.DatabaseConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfferService {

    // Retrieve all active offers
    public List<Offer> getAllActiveOffers() throws Exception {
        String query = """
                SELECT o.id, o.trip_id, o.discounted_price, o.details, o.is_active, o.offerphotos, o.host_id, t.trip_name
                FROM offers o
                LEFT JOIN trips t ON o.trip_id = t.trip_id
                WHERE o.is_active = true
                ORDER BY o.created_at DESC
                """;
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            List<Offer> offers = new ArrayList<>();
            while (resultSet.next()) {
                offers.add(createOfferFromResultSet(resultSet));
            }
            return offers;
        }
    }

    // Retrieve all offers for a specific host
    public List<Offer> getAllOffersForHost(int hostId) throws Exception {
        String query = """
                SELECT o.id, o.trip_id, o.discounted_price, o.details, o.is_active, o.offerphotos, o.host_id, t.trip_name
                FROM offers o
                LEFT JOIN trips t ON o.trip_id = t.trip_id
                WHERE o.host_id = ?
                ORDER BY o.created_at DESC
                """;
        List<Offer> offers = new ArrayList<>();
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, hostId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    offers.add(createOfferFromResultSet(resultSet));
                }
            }
        }
        return offers;
    }

    // Add a new offer
    public void addOffer(Offer offer) throws Exception {
        String query = "INSERT INTO offers (trip_id, discounted_price, details, is_active, offerphotos, host_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setOfferParameters(statement, offer);
            statement.executeUpdate();
        }
    }

    // Update an existing offer
    public void updateOffer(Offer offer) throws Exception {
        String query = "UPDATE offers SET trip_id = ?, discounted_price = ?, details = ?, is_active = ?, offerphotos = ?, host_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setOfferParameters(statement, offer);
            statement.setInt(7, offer.getId());
            statement.executeUpdate();
        }
    }

    // Retrieve an offer by ID
    public Offer getOfferById(int offerId) throws Exception {
        String query = """
                SELECT o.id, o.trip_id, o.discounted_price, o.details, o.is_active, o.offerphotos, o.host_id, t.trip_name
                FROM offers o
                LEFT JOIN trips t ON o.trip_id = t.trip_id
                WHERE o.id = ?
                """;
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, offerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createOfferFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    // Delete an offer by ID and host ID
    public void deleteOffer(int offerId, int hostId) throws Exception {
        String query = "DELETE FROM offers WHERE id = ? AND host_id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, offerId);
            statement.setInt(2, hostId);
            statement.executeUpdate();
        }
    }

    // Save an offer (add or update based on presence of ID)
    public void saveOffer(Offer offer) throws Exception {
        if (offer.getId() == 0) {
            addOffer(offer); // If no ID, it's a new offer
        } else {
            updateOffer(offer); // Otherwise, update existing
        }
    }

    // Retrieve all offers for a specific trip
    public List<Offer> getAllOffersForTrip(int tripId) throws Exception {
        String query = """
                SELECT o.id, o.trip_id, o.discounted_price, o.details, o.is_active, o.offerphotos, o.host_id, t.trip_name
                FROM offers o
                LEFT JOIN trips t ON o.trip_id = t.trip_id
                WHERE o.trip_id = ?
                """;
        List<Offer> offers = new ArrayList<>();
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, tripId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    offers.add(createOfferFromResultSet(resultSet));
                }
            }
        }
        return offers;
    }

    // Helper method to create an Offer object from a ResultSet
    private Offer createOfferFromResultSet(ResultSet resultSet) throws Exception {
        List<String> photoPaths = resultSet.getString("offerphotos") != null ? List.of(resultSet.getString("offerphotos").split(",")) : new ArrayList<>();
        return new Offer(
                resultSet.getInt("id"),
                resultSet.getInt("trip_id"),
                resultSet.getBigDecimal("discounted_price"),
                resultSet.getString("details"),
                resultSet.getBoolean("is_active"),
                photoPaths,
                resultSet.getInt("host_id"),
                resultSet.getString("trip_name") // Retrieve tripName from ResultSet
        );
    }

    // Helper method to set parameters for an Offer
    private void setOfferParameters(PreparedStatement statement, Offer offer) throws Exception {
        statement.setInt(1, offer.getTripId());
        statement.setBigDecimal(2, offer.getDiscountedPrice());
        statement.setString(3, offer.getDetails());
        statement.setBoolean(4, offer.isActive());
        statement.setString(5, String.join(",", offer.getPhotoPaths()));
        statement.setInt(6, offer.getHostId());
    }
}