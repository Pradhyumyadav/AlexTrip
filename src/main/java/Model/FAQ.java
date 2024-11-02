package Model;

import java.util.Objects;

public class FAQ {
    private final String title;
    private final String writtenDate;
    private FAQAnswer topAnswer;

    public FAQ(String title, String writtenDate, FAQAnswer topAnswer) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.writtenDate = Objects.requireNonNull(writtenDate, "Written date cannot be null");
        this.topAnswer = Objects.requireNonNull(topAnswer, "Top answer cannot be null");
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getWrittenDate() {
        return writtenDate;
    }

    public FAQAnswer getTopAnswer() {
        return topAnswer;
    }

    // Setter for topAnswer, if it can be updated
    public void setTopAnswer(FAQAnswer topAnswer) {
        this.topAnswer = Objects.requireNonNull(topAnswer, "Top answer cannot be null");
    }

    @Override
    public String toString() {
        return "FAQ{" +
                "title='" + title + '\'' +
                ", writtenDate='" + writtenDate + '\'' +
                ", topAnswer=" + topAnswer +
                '}';
    }
}