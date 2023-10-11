package com.iternova.ecommerce.service;

import com.iternova.ecommerce.exception.ProductException;
import com.iternova.ecommerce.model.Review;
import com.iternova.ecommerce.model.User;
import com.iternova.ecommerce.request.ReviewRequest;

import java.util.List;

public interface ReviewService {

    public Review createReview(ReviewRequest req, User user) throws ProductException;

    public List<Review> getAllReviews(Long productId);
}
