package kr.co.loosie.foody.interfaces;


import kr.co.loosie.foody.application.ReviewService;
import kr.co.loosie.foody.domain.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/restaurants/{restaurantId}/reviews")
    public ResponseEntity<?> create(
            @PathVariable("restaurantId") Long restaurantId,
            @Valid @RequestBody Review resource)
            throws URISyntaxException {
        Review review = reviewService.addReview(restaurantId,resource);
        String url = "/restaurants/" + restaurantId +
                "/reviews/" + review.getId();
        return ResponseEntity.created(new URI(url))
                .body("{}");
    }

}
