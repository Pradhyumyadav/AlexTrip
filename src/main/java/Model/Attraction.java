package Model;

import java.util.Objects;

public class Attraction {
    private final String title;
    private double rating;
    private int numberOfReviews;
    private final String primaryInfo;
    private String distance;
    private Photo cardPhoto;

    public Attraction(String title, double rating, int numberOfReviews, String primaryInfo, String distance, Photo cardPhoto) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.rating = rating;
        this.numberOfReviews = numberOfReviews;
        this.primaryInfo = Objects.requireNonNull(primaryInfo, "Primary info cannot be null");
        this.distance = distance != null ? distance : "N/A"; // Default to "N/A" if null
        this.cardPhoto = cardPhoto;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public double getRating() {
        return rating;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public String getPrimaryInfo() {
        return primaryInfo;
    }

    public String getDistance() {
        return distance;
    }

    public Photo getCardPhoto() {
        return cardPhoto;
    }

    // Setters for mutable fields
    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    public void setDistance(String distance) {
        this.distance = Objects.requireNonNullElse(distance, "N/A");
    }

    public void setCardPhoto(Photo cardPhoto) {
        this.cardPhoto = cardPhoto;
    }

    @Override
    public String toString() {
        return "Attraction{" +
                "title='" + title + '\'' +
                ", rating=" + rating +
                ", numberOfReviews=" + numberOfReviews +
                ", primaryInfo='" + primaryInfo + '\'' +
                ", distance='" + distance + '\'' +
                ", cardPhoto=" + cardPhoto +
                '}';
    }
}