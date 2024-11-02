package Model;

import java.util.Objects;

public class UserProfile {
    private String displayName;
    private String avatarUrl;

    public UserProfile(String displayName, String avatarUrl) {
        this.displayName = Objects.requireNonNull(displayName, "Display name cannot be null");
        this.avatarUrl = (avatarUrl != null && !avatarUrl.isEmpty()) ? avatarUrl : "https://via.placeholder.com/150";
    }

    // Getters and Setters with validation
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = Objects.requireNonNull(displayName, "Display name cannot be null");
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = (avatarUrl != null && !avatarUrl.isEmpty()) ? avatarUrl : "https://via.placeholder.com/150";
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "displayName='" + displayName + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(displayName, that.displayName) &&
                Objects.equals(avatarUrl, that.avatarUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, avatarUrl);
    }
}