package Model;

import java.util.Objects;

public class Restaurant {
    private String title;
    private double rating;
    private int numberOfReviews;
    private String primaryInfo;
    private String distance;
    private Photo cardPhoto;

    public Restaurant(String title, double rating, int numberOfReviews, String primaryInfo, String distance, Photo cardPhoto) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        setRating(rating);
        setNumberOfReviews(numberOfReviews);
        this.primaryInfo = primaryInfo;
        this.distance = distance;
        this.cardPhoto = cardPhoto;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
        this.rating = rating;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        if (numberOfReviews < 0) {
            throw new IllegalArgumentException("Number of reviews cannot be negative");
        }
        this.numberOfReviews = numberOfReviews;
    }

    public String getPrimaryInfo() {
        return primaryInfo;
    }

    public void setPrimaryInfo(String primaryInfo) {
        this.primaryInfo = primaryInfo;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Photo getCardPhoto() {
        return cardPhoto;
    }

    public void setCardPhoto(Photo cardPhoto) {
        this.cardPhoto = cardPhoto;
    }

    // Utility method for getting the photo URL with dimensions
    public String getCardPhotoUrlWithDimensions(int width, int height) {
        return cardPhoto != null ? cardPhoto.getUrlWithDimensions(width, height) : null;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "title='" + title + '\'' +
                ", rating=" + rating +
                ", numberOfReviews=" + numberOfReviews +
                ", primaryInfo='" + primaryInfo + '\'' +
                ", distance='" + distance + '\'' +
                ", cardPhoto=" + cardPhoto +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Double.compare(that.rating, rating) == 0 &&
                numberOfReviews == that.numberOfReviews &&
                Objects.equals(title, that.title) &&
                Objects.equals(primaryInfo, that.primaryInfo) &&
                Objects.equals(distance, that.distance) &&
                Objects.equals(cardPhoto, that.cardPhoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, rating, numberOfReviews, primaryInfo, distance, cardPhoto);
    }
}