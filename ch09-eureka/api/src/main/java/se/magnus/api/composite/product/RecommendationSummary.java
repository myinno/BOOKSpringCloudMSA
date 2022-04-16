package se.magnus.api.composite.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecommendationSummary {

//    private int recommendationId;
//    private String author;
//    private int rate;
//    private String content;
    private final int recommendationId;
    private final String author;
    private final int rate;
    private final String content;
    
    public RecommendationSummary() {
        this.recommendationId = 0;
        this.author = null;
        this.rate = 0;
        this.content = null;
    }
    
//    public RecommendationSummary(int recommendationId, String author, int rate) {
//        this.recommendationId = recommendationId;
//        this.author = author;
//        this.rate = rate;
//    }
//
//    public int getRecommendationId() {
//        return recommendationId;
//    }
//
//    public String getAuthor() {
//        return author;
//    }
//
//    public int getRate() {
//        return rate;
//    }
}
