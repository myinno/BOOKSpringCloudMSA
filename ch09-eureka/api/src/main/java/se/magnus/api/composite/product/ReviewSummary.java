package se.magnus.api.composite.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewSummary {

    private int reviewId;
    private String author;
    private String subject;
    private String content;
    
    public ReviewSummary() {
        this.reviewId = 0;
        this.author = null;
        this.subject = null;
        this.content = null;
    }
    
//    public ReviewSummary(int reviewId, String author, String subject) {
//        this.reviewId = reviewId;
//        this.author = author;
//        this.subject = subject;
//    }
//
//    public int getReviewId() {
//        return reviewId;
//    }
//
//    public String getAuthor() {
//        return author;
//    }
//
//    public String getSubject() {
//        return subject;
//    }
}
