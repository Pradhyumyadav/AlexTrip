package Model;

import java.util.Objects;

public class Photo {
    private int photoId; // Unique identifier for the photo
    private String filePath; // Path of the photo file, stored in the database
    private String description; // Description of the photo
    private String altText; // Alternative text for accessibility

    // Constructor
    public Photo(int photoId, String filePath, String description, String altText) {
        this.photoId = photoId;
        this.setFilePath(filePath); // Use setter to apply path validation
        this.description = description;
        this.altText = altText;
    }

    // Default constructor for flexibility
    public Photo() {}

    // Getters and Setters
    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        if (!isValidFilePath(filePath)) {
            throw new IllegalArgumentException("Invalid file path format.");
        }
        this.filePath = filePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    // Utility method to validate the file path format
    public static boolean isValidFilePath(String filePath) {
        // Regex checks for a path that starts with '/uploaded_photos/', followed by a timestamp, host ID, and a valid filename
        return filePath != null && filePath.matches("^/uploaded_photos/\\d+_(\\d+)_.*\\.(jpg|jpeg|png|gif)$");
    }

    // ToString for debugging or logging purposes
    @Override
    public String toString() {
        return "Photo{" +
                "photoId=" + photoId +
                ", filePath='" + filePath + '\'' +
                ", description='" + description + '\'' +
                ", altText='" + altText + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return photoId == photo.photoId &&
                Objects.equals(filePath, photo.filePath) &&
                Objects.equals(description, photo.description) &&
                Objects.equals(altText, photo.altText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoId, filePath, description, altText);
    }
}