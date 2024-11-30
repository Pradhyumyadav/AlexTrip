package Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Offer {
    private int id;
    private int tripId;
    private BigDecimal discountedPrice;
    private String details;
    private boolean isActive; // Properly handle the boolean naming
    private List<String> photoPaths;
    private int hostId;
    private String tripName; // New field to store the trip name

    // Default Constructor
    public Offer() {
        this.photoPaths = new ArrayList<>();
    }

    // Parametrized Constructor
    public Offer(int id, int tripId, BigDecimal discountedPrice, String details, boolean isActive, List<String> photoPaths, int hostId, String tripName) {
        this.id = id;
        this.tripId = tripId;
        this.discountedPrice = discountedPrice;
        this.details = details;
        this.isActive = isActive;
        this.photoPaths = photoPaths != null ? new ArrayList<>(photoPaths) : new ArrayList<>();
        this.hostId = hostId;
        this.tripName = tripName; // Initialize tripName
    }

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

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    // Follow JavaBeans naming convention for boolean getters
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public List<String> getPhotoPaths() {
        return photoPaths;
    }

    public void setPhotoPaths(List<String> photoPaths) {
        this.photoPaths = photoPaths != null ? new ArrayList<>(photoPaths) : new ArrayList<>();
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", tripId=" + tripId +
                ", discountedPrice=" + discountedPrice +
                ", details='" + details + '\'' +
                ", isActive=" + isActive +
                ", photoPaths=" + photoPaths +
                ", hostId=" + hostId +
                ", tripName='" + tripName + '\'' +
                '}';
    }

    // Additional Utility Methods
    public String getPhotosAsString() {
        return String.join(",", photoPaths);
    }
}