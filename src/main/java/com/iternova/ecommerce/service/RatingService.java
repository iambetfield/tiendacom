package com.iternova.ecommerce.service;

import com.iternova.ecommerce.exception.ProductException;
import com.iternova.ecommerce.model.Rating;
import com.iternova.ecommerce.model.User;
import com.iternova.ecommerce.request.RatingRequest;

import java.util.List;

public interface RatingService {

    public Rating createRating(RatingRequest req, User user) throws ProductException;

    public List<Rating> getProductsRating(Long productId);
}
