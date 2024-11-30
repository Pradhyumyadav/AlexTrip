package Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Trip implements Serializable {
    private int tripId;
    private String tripName;
    private String destination;
    private int duration;
    private BigDecimal price;
    private String activityType;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private int maxParticipants;
    private List<String> photoPaths;
    private int hostId;
    private String cancellationPolicy;
    private String hostName;
    private String hostContactEmail;
    private String hostContactPhone;
    private String itinerary;
    private String inclusions;
    private String exclusions;
    private String difficultyLevel;
    private String packingList;
    private LocalDate bookingDeadline;
    private String paymentTerms;

    // Default constructor
    public Trip() {
        this.photoPaths = new ArrayList<>();
    }

    // Full constructor
    public Trip(int tripId, String tripName, String destination, int duration, BigDecimal price, String activityType,
                String description, LocalDate startDate, LocalDate endDate, int maxParticipants, List<String> photoPaths,
                int hostId, String cancellationPolicy, String itinerary, String inclusions, String exclusions,
                String difficultyLevel, String packingList, LocalDate bookingDeadline, String paymentTerms,
                String hostName, String hostContactEmail, String hostContactPhone) {
        this.tripId = tripId;
        this.tripName = tripName;
        this.destination = destination;
        this.duration = duration;
        this.price = price;
        this.activityType = activityType;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxParticipants = maxParticipants;
        this.photoPaths = photoPaths != null ? photoPaths : new ArrayList<>();
        this.hostId = hostId;
        this.cancellationPolicy = cancellationPolicy;
        this.itinerary = itinerary;
        this.inclusions = inclusions;
        this.exclusions = exclusions;
        this.difficultyLevel = difficultyLevel;
        this.packingList = packingList;
        this.bookingDeadline = bookingDeadline;
        this.paymentTerms = paymentTerms;
        this.hostName = hostName;
        this.hostContactEmail = hostContactEmail;
        this.hostContactPhone = hostContactPhone;
    }

    // Utility methods for photo paths
    public static String fromListToCommaSeparatedString(List<String> photoPaths) {
        if (photoPaths == null || photoPaths.isEmpty()) {
            return "";
        }
        return String.join(",", photoPaths);
    }

    public static List<String> fromCommaSeparatedStringToList(String photoPathsString) {
        List<String> list = new ArrayList<>();
        if (photoPathsString != null && !photoPathsString.isEmpty()) {
            String[] paths = photoPathsString.split(",");
            for (String path : paths) {
                list.add(path.trim());
            }
        }
        return list;
    }

    // Getters and setters for all fields
    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public List<String> getPhotoPaths() {
        return photoPaths;
    }

    public void setPhotoPaths(List<String> photoPaths) {
        this.photoPaths = photoPaths;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostContactEmail() {
        return hostContactEmail;
    }

    public void setHostContactEmail(String hostContactEmail) {
        this.hostContactEmail = hostContactEmail;
    }

    public String getHostContactPhone() {
        return hostContactPhone;
    }

    public void setHostContactPhone(String hostContactPhone) {
        this.hostContactPhone = hostContactPhone;
    }

    public String getItinerary() {
        return itinerary;
    }

    public void setItinerary(String itinerary) {
        this.itinerary = itinerary;
    }

    public String getInclusions() {
        return inclusions;
    }

    public void setInclusions(String inclusions) {
        this.inclusions = inclusions;
    }

    public String getExclusions() {
        return exclusions;
    }

    public void setExclusions(String exclusions) {
        this.exclusions = exclusions;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getPackingList() {
        return packingList;
    }

    public void setPackingList(String packingList) {
        this.packingList = packingList;
    }

    public LocalDate getBookingDeadline() {
        return bookingDeadline;
    }

    public void setBookingDeadline(LocalDate bookingDeadline) {
        this.bookingDeadline = bookingDeadline;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return tripId == trip.tripId &&
                duration == trip.duration &&
                maxParticipants == trip.maxParticipants &&
                hostId == trip.hostId &&
                Objects.equals(tripName, trip.tripName) &&
                Objects.equals(destination, trip.destination) &&
                Objects.equals(price, trip.price) &&
                Objects.equals(activityType, trip.activityType) &&
                Objects.equals(description, trip.description) &&
                Objects.equals(startDate, trip.startDate) &&
                Objects.equals(endDate, trip.endDate) &&
                Objects.equals(photoPaths, trip.photoPaths) &&
                Objects.equals(cancellationPolicy, trip.cancellationPolicy) &&
                Objects.equals(hostName, trip.hostName) &&
                Objects.equals(hostContactEmail, trip.hostContactEmail) &&
                Objects.equals(hostContactPhone, trip.hostContactPhone) &&
                Objects.equals(itinerary, trip.itinerary) &&
                Objects.equals(inclusions, trip.inclusions) &&
                Objects.equals(exclusions, trip.exclusions) &&
                Objects.equals(difficultyLevel, trip.difficultyLevel) &&
                Objects.equals(packingList, trip.packingList) &&
                Objects.equals(bookingDeadline, trip.bookingDeadline) &&
                Objects.equals(paymentTerms, trip.paymentTerms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tripId, tripName, destination, duration, price, activityType, description, startDate, endDate, maxParticipants, photoPaths, hostId, cancellationPolicy, hostName, hostContactEmail, hostContactPhone, itinerary, inclusions, exclusions, difficultyLevel, packingList, bookingDeadline, paymentTerms);
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripId=" + tripId +
                ", tripName='" + tripName + '\'' +
                ", destination='" + destination + '\'' +
                ", duration=" + duration +
                ", price=" + price +
                ", activityType='" + activityType + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", maxParticipants=" + maxParticipants +
                ", photoPaths=" + photoPaths +
                ", hostId=" + hostId +
                ", cancellationPolicy='" + cancellationPolicy + '\'' +
                ", hostName='" + hostName + '\'' +
                ", hostContactEmail='" + hostContactEmail + '\'' +
                ", hostContactPhone='" + hostContactPhone + '\'' +
                ", itinerary='" + itinerary + '\'' +
                ", inclusions='" + inclusions + '\'' +
                ", exclusions='" + exclusions + '\'' +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", packingList='" + packingList + '\'' +
                ", bookingDeadline=" + bookingDeadline +
                ", paymentTerms='" + paymentTerms + '\'' +
                '}';
    }
}