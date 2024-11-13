package Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private final String id; // Unique hotel ID for your application
    private final String placeId; // Place ID from Google Places API
    private final String name;
    private final String description;
    private final String location;
    private final double latitude;
    private final double longitude;
    private final double rating;
    private final int numberReviews;
    private final BigDecimal price;
    private final String activityType; // New field for activity type
    private final int stayDuration; // Field for stay duration
    private List<String> imageUrls; // Dynamically set list of image URLs

    public Hotel(Builder builder) {
        this.id = builder.id;
        this.placeId = builder.placeId;
        this.name = builder.name;
        this.description = builder.description;
        this.location = builder.location;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.rating = builder.rating;
        this.numberReviews = builder.numberReviews;
        this.price = builder.price;
        this.activityType = builder.activityType;
        this.stayDuration = builder.stayDuration;
        this.imageUrls = new ArrayList<>(builder.imageUrls); // Copy of the image URL list to ensure encapsulation
    }

    // Getters for all properties
    public String getId() {
        return id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRating() {
        return rating;
    }

    public int getNumberReviews() {
        return numberReviews;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getActivityType() {
        return activityType;
    }

    public int getStayDuration() {
        return stayDuration;
    }

    public List<String> getImageUrls() {
        return new ArrayList<>(imageUrls); // Return a copy of the list to prevent external modifications
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = new ArrayList<>(imageUrls); // Set a copy of the list to ensure encapsulation
    }

    // Builder class
    public static class Builder {
        private String id;
        private String placeId;
        private String name;
        private String description;
        private String location;
        private double latitude;
        private double longitude;
        private double rating;
        private int numberReviews;
        private BigDecimal price;
        private String activityType; // Field for activity type
        private int stayDuration; // Field for stay duration
        private List<String> imageUrls = new ArrayList<>();

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder placeId(String placeId) {
            this.placeId = placeId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder rating(double rating) {
            this.rating = rating;
            return this;
        }

        public Builder numberReviews(int numberReviews) {
            this.numberReviews = numberReviews;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder activityType(String activityType) {
            this.activityType = activityType;
            return this;
        }

        public Builder stayDuration(int stayDuration) {
            this.stayDuration = stayDuration;
            return this;
        }

        public Builder imageUrls(List<String> imageUrls) {
            this.imageUrls = new ArrayList<>(imageUrls); // Ensuring encapsulation by copying the list
            return this;
        }

        public Hotel build() {
            return new Hotel(this);
        }
    }
}