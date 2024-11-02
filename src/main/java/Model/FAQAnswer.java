package Model;

import java.util.Objects;

public class FAQAnswer {
    private final String text;
    private final String writtenDate;
    private int thumbsUpCount;
    private final UserProfile memberProfile;

    public FAQAnswer(String text, String writtenDate, int thumbsUpCount, UserProfile memberProfile) {
        this.text = Objects.requireNonNull(text, "Text cannot be null");
        this.writtenDate = Objects.requireNonNull(writtenDate, "Written date cannot be null");
        this.thumbsUpCount = thumbsUpCount;
        this.memberProfile = Objects.requireNonNull(memberProfile, "Member profile cannot be null");
    }

    // Getters
    public String getText() {
        return text;
    }

    public String getWrittenDate() {
        return writtenDate;
    }

    public int getThumbsUpCount() {
        return thumbsUpCount;
    }

    public UserProfile getMemberProfile() {
        return memberProfile;
    }

    // Setter for thumbsUpCount, if it can be updated
    public void setThumbsUpCount(int thumbsUpCount) {
        this.thumbsUpCount = thumbsUpCount;
    }

    @Override
    public String toString() {
        return "FAQAnswer{" +
                "text='" + text + '\'' +
                ", writtenDate='" + writtenDate + '\'' +
                ", thumbsUpCount=" + thumbsUpCount +
                ", memberProfile=" + memberProfile +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FAQAnswer)) return false;
        FAQAnswer that = (FAQAnswer) o;
        return thumbsUpCount == that.thumbsUpCount &&
                text.equals(that.text) &&
                writtenDate.equals(that.writtenDate) &&
                memberProfile.equals(that.memberProfile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, writtenDate, thumbsUpCount, memberProfile);
    }
}