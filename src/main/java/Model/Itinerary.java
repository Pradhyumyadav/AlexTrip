package Model;

public class Itinerary {
    private int tripId;            // Foreign key to link this itinerary to a specific trip
    private int dayNumber;         // Day of the itinerary
    private String title;          // Title or short summary of the day's activity
    private String description;    // Detailed description of the day's activity
    private String imageUrl;       // URL for a representative image of the activity
    private String location;       // Location where the activity occurs
    private String highlights;     // Key highlights or unique aspects of the day's activity
    private String activityType;   // Type of activity for the day (e.g., hiking, cultural visit, etc.)
    private String notes;          // Additional notes or information about the activity (e.g., what to bring)

    // Default constructor for flexibility
    public Itinerary() {}

    // Constructor with all fields
    public Itinerary(int tripId, int dayNumber, String title, String description, String imageUrl,
                     String location, String highlights, String activityType, String notes) {
        this.tripId = tripId;
        this.dayNumber = dayNumber;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.location = location;
        this.highlights = highlights;
        this.activityType = activityType;
        this.notes = notes;
    }

    // Getters and Setters
    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.matches("^https?://.*")) {
            throw new IllegalArgumentException("Invalid image URL format");
        }
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHighlights() {
        return highlights;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Utility method for validation
    public boolean isValidItineraryItem() {
        return tripId > 0 && dayNumber > 0 && title != null && !title.isEmpty();
    }

    // ToString method for debugging
    @Override
    public String toString() {
        return "ItineraryItem{" +
                "tripId=" + tripId +
                ", dayNumber=" + dayNumber +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", location='" + location + '\'' +
                ", highlights='" + highlights + '\'' +
                ", activityType='" + activityType + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}