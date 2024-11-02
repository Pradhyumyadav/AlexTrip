package Model;

import java.util.Objects;

public class Photo {
    private final int maxHeight;
    private final int maxWidth;
    private final String urlTemplate;

    private Photo(Builder builder) {
        this.maxHeight = builder.maxHeight;
        this.maxWidth = builder.maxWidth;
        this.urlTemplate = builder.urlTemplate;
    }

    public static class Builder {
        private int maxHeight;
        private int maxWidth;
        private String urlTemplate;

        public Builder maxHeight(int maxHeight) {
            if (maxHeight <= 0) throw new IllegalArgumentException("maxHeight must be positive");
            this.maxHeight = maxHeight;
            return this;
        }

        public Builder maxWidth(int maxWidth) {
            if (maxWidth <= 0) throw new IllegalArgumentException("maxWidth must be positive");
            this.maxWidth = maxWidth;
            return this;
        }

        public Builder urlTemplate(String urlTemplate) {
            this.urlTemplate = Objects.requireNonNull(urlTemplate, "urlTemplate cannot be null");
            return this;
        }

        public Photo build() {
            if (maxHeight <= 0) throw new IllegalStateException("maxHeight must be set and positive");
            if (maxWidth <= 0) throw new IllegalStateException("maxWidth must be set and positive");
            if (urlTemplate == null) throw new IllegalStateException("urlTemplate must be set");
            return new Photo(this);
        }
    }

    // Getters (no setters to maintain immutability)
    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    // Utility method to get the URL with specific dimensions
    public String getUrlWithDimensions(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        if (width > maxWidth || height > maxHeight) {
            throw new IllegalArgumentException("Requested dimensions exceed maximum allowed");
        }
        return urlTemplate.replace("{width}", String.valueOf(width))
                .replace("{height}", String.valueOf(height));
    }

    @Override
    public String toString() {
        return "Photo{" +
                "maxHeight=" + maxHeight +
                ", maxWidth=" + maxWidth +
                ", urlTemplate='" + urlTemplate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return maxHeight == photo.maxHeight &&
                maxWidth == photo.maxWidth &&
                Objects.equals(urlTemplate, photo.urlTemplate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxHeight, maxWidth, urlTemplate);
    }
}