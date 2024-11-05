package Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Hotel {
    private final String id;
    private final String name;
    private final String description;
    private final String location;
    private final double latitude;
    private final double longitude;
    private final String countryCode;
    private final double rating;
    private final int numberReviews;
    private final BigDecimal price;
    private BigDecimal convertedPriceGBP; // Field for storing price in GBP
    private final String activityType;
    private final int stayDuration;
    private final List<String> imageUrls;
    private final List<Review> reviews;
    private final List<Restaurant> restaurantsNearby;
    private final List<Attraction> attractionsNearby;
    private final List<FAQ> faqs;

    private Hotel(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "ID cannot be null");
        this.name = Objects.requireNonNull(builder.name, "Name cannot be null");
        this.description = Objects.requireNonNull(builder.description, "Description cannot be null");
        this.location = Objects.requireNonNull(builder.location, "Location cannot be null");
        this.latitude = validateLatitude(builder.latitude);
        this.longitude = validateLongitude(builder.longitude);
        this.countryCode = builder.countryCode != null ? builder.countryCode : "N/A"; // Optional
        this.rating = validateRating(builder.rating);
        this.numberReviews = validateNumberReviews(builder.numberReviews);
        this.price = Objects.requireNonNull(builder.price, "Price cannot be null");
        this.convertedPriceGBP = builder.convertedPriceGBP;
        this.activityType = builder.activityType != null ? builder.activityType : "General";
        this.stayDuration = validateStayDuration(builder.stayDuration);
        this.imageUrls = builder.imageUrls != null ? List.copyOf(builder.imageUrls) : List.of("https://via.placeholder.com/400x300?text=No+Image+Available");
        this.reviews = List.copyOf(builder.reviews != null ? builder.reviews : new ArrayList<>());
        this.restaurantsNearby = List.copyOf(builder.restaurantsNearby != null ? builder.restaurantsNearby : new ArrayList<>());
        this.attractionsNearby = List.copyOf(builder.attractionsNearby != null ? builder.attractionsNearby : new ArrayList<>());
        this.faqs = List.copyOf(builder.faqs != null ? builder.faqs : new ArrayList<>());
    }

    // Validation methods
    private static double validateLatitude(double latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees");
        }
        return latitude;
    }

    private static double validateLongitude(double longitude) {
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees");
        }
        return longitude;
    }

    private static double validateRating(double rating) {
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
        return rating;
    }

    private static int validateNumberReviews(int numberReviews) {
        if (numberReviews < 0) {
            throw new IllegalArgumentException("Number of reviews cannot be negative");
        }
        return numberReviews;
    }

    private static int validateStayDuration(int stayDuration) {
        if (stayDuration < 1) {
            throw new IllegalArgumentException("Stay duration must be at least 1 day");
        }
        return stayDuration;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getCountryCode() { return countryCode; }
    public double getRating() { return rating; }
    public int getNumberReviews() { return numberReviews; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getConvertedPriceGBP() { return convertedPriceGBP; }
    public String getActivityType() { return activityType; }
    public int getStayDuration() { return stayDuration; }
    public List<String> getImageUrls() { return imageUrls; }
    public List<Review> getReviews() { return reviews; }
    public List<Restaurant> getRestaurantsNearby() { return restaurantsNearby; }
    public List<Attraction> getAttractionsNearby() { return attractionsNearby; }
    public List<FAQ> getFaqs() { return faqs; }

    // Setter for converted price in GBP
    public void setConvertedPriceGBP(BigDecimal convertedPriceGBP) {
        this.convertedPriceGBP = convertedPriceGBP;
    }

    // Builder class
    public static class Builder {
        private String id;
        private String name;
        private String description;
        private String location;
        private double latitude;
        private double longitude;
        private String countryCode;
        private double rating;
        private int numberReviews;
        private BigDecimal price;
        private BigDecimal convertedPriceGBP;
        private String activityType;
        private int stayDuration;
        private List<String> imageUrls;
        private List<Review> reviews;
        private List<Restaurant> restaurantsNearby;
        private List<Attraction> attractionsNearby;
        private List<FAQ> faqs;

        public Builder() {
            // Set default values for optional fields
            this.convertedPriceGBP = BigDecimal.ZERO;
            this.activityType = "General";
            this.stayDuration = 1;
            this.imageUrls = new ArrayList<>();
            this.reviews = new ArrayList<>();
            this.restaurantsNearby = new ArrayList<>();
            this.attractionsNearby = new ArrayList<>();
            this.faqs = new ArrayList<>();
        }

        public Builder id(String id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder location(String location) { this.location = location; return this; }
        public Builder latitude(double latitude) { this.latitude = latitude; return this; }
        public Builder longitude(double longitude) { this.longitude = longitude; return this; }
        public Builder countryCode(String countryCode) { this.countryCode = countryCode; return this; }
        public Builder rating(double rating) { this.rating = rating; return this; }
        public Builder numberReviews(int numberReviews) { this.numberReviews = numberReviews; return this; }
        public Builder price(BigDecimal price) { this.price = price; return this; }
        public Builder convertedPriceGBP(BigDecimal convertedPriceGBP) { this.convertedPriceGBP = convertedPriceGBP; return this; }
        public Builder activityType(String activityType) { this.activityType = activityType; return this; }
        public Builder stayDuration(int stayDuration) { this.stayDuration = stayDuration; return this; }
        public Builder imageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; return this; }
        public Builder imageUrl(String imageUrl) {
            if (this.imageUrls == null) {
                this.imageUrls = new ArrayList<>();
            }
            this.imageUrls.add(imageUrl);
            return this;
        }
        public Builder reviews(List<Review> reviews) { this.reviews = reviews; return this; }
        public Builder restaurantsNearby(List<Restaurant> restaurantsNearby) { this.restaurantsNearby = restaurantsNearby; return this; }
        public Builder attractionsNearby(List<Attraction> attractionsNearby) { this.attractionsNearby = attractionsNearby; return this; }
        public Builder faqs(List<FAQ> faqs) { this.faqs = faqs; return this; }

        public Hotel build() { return new Hotel(this); }
    }
}