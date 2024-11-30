package Servlet;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class BookingDetails implements Serializable {
    private int bookingId;
    private int numParticipants;
    private Date bookingDate;
    private String customerName;
    private String customerEmail;
    private String specialRequests;
    private double totalPrice;
    private String tripName;
    private Date startDate;
    private Date endDate;
    private String cancellationPolicy;  // To display cancellation policy
    private List<String> photoPaths;   // To display trip photos
    private String tripDescription;    // To display trip description
    private String activityType;       // To display the activity type

    // Default Constructor
    public BookingDetails() {
    }

    // Full Constructor
    public BookingDetails(int bookingId, int numParticipants, Date bookingDate, String customerName,
                          String customerEmail, String specialRequests, double totalPrice, String tripName,
                          Date startDate, Date endDate, String cancellationPolicy, List<String> photoPaths,
                          String tripDescription, String activityType) {
        this.bookingId = bookingId;
        this.numParticipants = Math.max(numParticipants, 0); // Ensure non-negative participants
        this.bookingDate = bookingDate;
        this.customerName = trimOrDefault(customerName);
        this.customerEmail = trimOrDefault(customerEmail);
        this.specialRequests = trimOrDefault(specialRequests);
        this.totalPrice = Math.max(totalPrice, 0); // Ensure non-negative price
        this.tripName = trimOrDefault(tripName);
        this.startDate = startDate;
        this.endDate = endDate;
        this.cancellationPolicy = trimOrDefault(cancellationPolicy);
        this.photoPaths = photoPaths;
        this.tripDescription = trimOrDefault(tripDescription);
        this.activityType = trimOrDefault(activityType);
    }

    // Utility method for trimming strings or setting defaults
    private String trimOrDefault(String value) {
        return value != null ? value.trim() : "";
    }

    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getNumParticipants() {
        return numParticipants;
    }

    public void setNumParticipants(int numParticipants) {
        this.numParticipants = Math.max(numParticipants, 0); // Ensure non-negative participants
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = trimOrDefault(customerName);
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = trimOrDefault(customerEmail);
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = trimOrDefault(specialRequests);
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = Math.max(totalPrice, 0); // Ensure non-negative price
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = trimOrDefault(tripName);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = trimOrDefault(cancellationPolicy);
    }

    public List<String> getPhotoPaths() {
        return photoPaths;
    }

    public void setPhotoPaths(List<String> photoPaths) {
        this.photoPaths = photoPaths;
    }

    public String getTripDescription() {
        return tripDescription;
    }

    public void setTripDescription(String tripDescription) {
        this.tripDescription = trimOrDefault(tripDescription);
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = trimOrDefault(activityType);
    }

    // toString Method
    @Override
    public String toString() {
        return "BookingDetails{" +
                "bookingId=" + bookingId +
                ", numParticipants=" + numParticipants +
                ", bookingDate=" + bookingDate +
                ", customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", specialRequests='" + specialRequests + '\'' +
                ", totalPrice=" + totalPrice +
                ", tripName='" + tripName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", cancellationPolicy='" + cancellationPolicy + '\'' +
                ", photoPaths=" + photoPaths +
                ", tripDescription='" + tripDescription + '\'' +
                ", activityType='" + activityType + '\'' +
                '}';
    }

    // equals and hashCode methods for comparison and collections
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingDetails that)) return false;
        return bookingId == that.bookingId &&
                numParticipants == that.numParticipants &&
                Double.compare(that.totalPrice, totalPrice) == 0 &&
                Objects.equals(bookingDate, that.bookingDate) &&
                Objects.equals(customerName, that.customerName) &&
                Objects.equals(customerEmail, that.customerEmail) &&
                Objects.equals(specialRequests, that.specialRequests) &&
                Objects.equals(tripName, that.tripName) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(cancellationPolicy, that.cancellationPolicy) &&
                Objects.equals(photoPaths, that.photoPaths) &&
                Objects.equals(tripDescription, that.tripDescription) &&
                Objects.equals(activityType, that.activityType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, numParticipants, bookingDate, customerName, customerEmail, specialRequests, totalPrice, tripName, startDate, endDate, cancellationPolicy, photoPaths, tripDescription, activityType);
    }
}