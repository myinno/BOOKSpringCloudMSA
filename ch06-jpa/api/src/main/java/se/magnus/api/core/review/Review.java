package se.magnus.api.core.review;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Review {
    private int productId;
    private int reviewId;
    private String author;
    private String subject;
    private String content;
    private String serviceAddress;

    public Review() {
        productId = 0;
        reviewId = 0;
        author = null;
        subject = null;
        content = null;
        serviceAddress = null;
    }

//    public Review(int productId, int reviewId, String author, String subject, String content, String serviceAddress) {
//        this.productId = productId;
//        this.reviewId = reviewId;
//        this.author = author;
//        this.subject = subject;
//        this.content = content;
//        this.serviceAddress = serviceAddress;
//    }
//
//    public int getProductId() {
//        return productId;
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
//
//    public String getContent() {
//        return content;
//    }
//
//    public String getServiceAddress() {
//        return serviceAddress;
//    }
}
