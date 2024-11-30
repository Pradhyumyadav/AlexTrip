package Model;

import java.time.LocalDateTime;

public class Review {
    private int id; // Unique ID for the review
    private int tripId; // Foreign key to associate with a Trip
    private int rating; // Rating given in the review (1-5 scale)
    private String reviewText; // Text of the review
    private String reviewerName; // Name of the reviewer
    private LocalDateTime timestamp; // When the review was submitted

    // Constructor with all fields
    public Review(int id, int tripId, int rating, String reviewText, String reviewerName, LocalDateTime timestamp) {
        this.id = id;
        this.tripId = tripId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewerName = reviewerName;
        this.timestamp = timestamp;
    }

    // Overloaded constructor for flexibility (without ID for new reviews)
    public Review(int tripId, int rating, String reviewText, String reviewerName, LocalDateTime timestamp) {
        this.tripId = tripId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewerName = reviewerName;
        this.timestamp = timestamp;
    }

    // Default constructor
    public Review() {}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // Helper method to validate the review
    public boolean isValidReview() {
        return rating >= 1 && rating <= 5 && reviewText != null && !reviewText.trim().isEmpty() && reviewerName != null;
    }

    // ToString for debugging or logging purposes
    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", tripId=" + tripId +
                ", rating=" + rating +
                ", reviewText='" + reviewText + '\'' +
                ", reviewerName='" + reviewerName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}