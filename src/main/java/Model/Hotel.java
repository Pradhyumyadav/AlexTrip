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
        this.countryCode = Objects.requireNonNull(builder.countryCode, "Country code cannot be null");
        this.rating = validateRating(builder.rating);
        this.numberReviews = validateNumberReviews(builder.numberReviews);
        this.price = Objects.requireNonNull(builder.price, "Price cannot be null");
        this.activityType = Objects.requireNonNull(builder.activityType, "Activity type cannot be null");
        this.stayDuration = validateStayDuration(builder.stayDuration);

        // Default placeholder for imageUrls if empty
        if (builder.imageUrls == null || builder.imageUrls.isEmpty()) {
            this.imageUrls = List.of("https://via.placeholder.com/400x300?text=No+Image+Available");
        } else {
            this.imageUrls = Collections.unmodifiableList(new ArrayList<>(builder.imageUrls));
        }

        // Initialize other lists with defensive copies
        this.reviews = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(builder.reviews, "Reviews list cannot be null")));
        this.restaurantsNearby = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(builder.restaurantsNearby, "Restaurants list cannot be null")));
        this.attractionsNearby = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(builder.attractionsNearby, "Attractions list cannot be null")));
        this.faqs = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(builder.faqs, "FAQs list cannot be null")));
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

    // Getters remain unchanged as they were already immutable
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
    public String getActivityType() { return activityType; }
    public int getStayDuration() { return stayDuration; }
    public List<String> getImageUrls() { return imageUrls; }
    public List<Review> getReviews() { return reviews; }
    public List<Restaurant> getRestaurantsNearby() { return restaurantsNearby; }
    public List<Attraction> getAttractionsNearby() { return attractionsNearby; }
    public List<FAQ> getFaqs() { return faqs; }

    // Builder class (no changes needed)
    public static class Builder {
        private String id = "N/A";
        private String name = "Unnamed Hotel";
        private String description = "No description available.";
        private String location = "Unknown location";
        private double latitude = 0.0;
        private double longitude = 0.0;
        private String countryCode = "N/A";
        private double rating = 0.0;
        private int numberReviews = 0;
        private BigDecimal price = BigDecimal.ZERO;
        private String activityType = "General";
        private int stayDuration = 1;
        private List<String> imageUrls = new ArrayList<>();
        private List<Review> reviews = new ArrayList<>();
        private List<Restaurant> restaurantsNearby = new ArrayList<>();
        private List<Attraction> attractionsNearby = new ArrayList<>();
        private List<FAQ> faqs = new ArrayList<>();

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
        public Builder activityType(String activityType) { this.activityType = activityType; return this; }
        public Builder stayDuration(int stayDuration) { this.stayDuration = stayDuration; return this; }
        public Builder imageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; return this; }
        public Builder reviews(List<Review> reviews) { this.reviews = reviews; return this; }
        public Builder restaurantsNearby(List<Restaurant> restaurantsNearby) { this.restaurantsNearby = restaurantsNearby; return this; }
        public Builder attractionsNearby(List<Attraction> attractionsNearby) { this.attractionsNearby = attractionsNearby; return this; }
        public Builder faqs(List<FAQ> faqs) { this.faqs = faqs; return this; }

        public Hotel build() { return new Hotel(this); }
    }
}