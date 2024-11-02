package Model;

import java.time.LocalDate;
import java.util.Objects;

public class Review {
    private String title;
    private String text;
    private LocalDate publishedDate;
    private UserProfile userProfile;
    private double rating;  // Added rating field

    public Review(String title, String text, LocalDate publishedDate, UserProfile userProfile, double rating) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.text = Objects.requireNonNull(text, "Text cannot be null");
        this.publishedDate = Objects.requireNonNull(publishedDate, "Published date cannot be null");
        this.userProfile = Objects.requireNonNull(userProfile, "User profile cannot be null");
        this.rating = validateRating(rating);
    }

    // Getters and Setters with basic validation
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = Objects.requireNonNull(text, "Text cannot be null");
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = Objects.requireNonNull(publishedDate, "Published date cannot be null");
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = Objects.requireNonNull(userProfile, "User profile cannot be null");
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = validateRating(rating);
    }

    private double validateRating(double rating) {
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
        return rating;
    }

    @Override
    public String toString() {
        return "Review{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", publishedDate=" + publishedDate +
                ", userProfile=" + userProfile +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Double.compare(review.rating, rating) == 0 &&
                Objects.equals(title, review.title) &&
                Objects.equals(text, review.text) &&
                Objects.equals(publishedDate, review.publishedDate) &&
                Objects.equals(userProfile, review.userProfile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, text, publishedDate, userProfile, rating);
    }

    public Comparable<Object> getDate() {
        return null;
    }
}

