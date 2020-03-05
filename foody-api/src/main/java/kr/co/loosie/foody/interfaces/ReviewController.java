package kr.co.loosie.foody.interfaces;


import kr.co.loosie.foody.application.ReviewService;
import kr.co.loosie.foody.domain.Review;
import org.h2.command.ddl.CreateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class ReviewController {

    @Autowired
    private ReviewService reviewService = new ReviewService();

    @PostMapping("/restaurants/{restaurantId}/reviews")
    public ResponseEntity<?> create() throws URISyntaxException {
        Review review = Review.builder().build();
        reviewService.addReview(review);
        return ResponseEntity.created(new URI("/restaurants/1/reviews/1"))
                .body("{}");
    }

}
