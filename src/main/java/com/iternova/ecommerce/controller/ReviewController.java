package com.iternova.ecommerce.controller;

import com.iternova.ecommerce.exception.ProductException;
import com.iternova.ecommerce.exception.UserException;
import com.iternova.ecommerce.model.Rating;
import com.iternova.ecommerce.model.Review;
import com.iternova.ecommerce.model.User;
import com.iternova.ecommerce.request.RatingRequest;
import com.iternova.ecommerce.request.ReviewRequest;
import com.iternova.ecommerce.service.RatingService;
import com.iternova.ecommerce.service.ReviewService;
import com.iternova.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest req, @RequestHeader("Authorization")String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);

       Review review = reviewService.createReview(req,user);

        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getProductReviews(@PathVariable Long productId, @RequestHeader("Authorization") String jwt)
            throws UserException, ProductException{

        User user = userService.findUserProfileByJwt(jwt);
        List<Review> reviews = reviewService.getAllReviews(user.getId());

        return new ResponseEntity<>(reviews,HttpStatus.CREATED);
    }
}
