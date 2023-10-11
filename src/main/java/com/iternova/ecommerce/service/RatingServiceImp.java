package com.iternova.ecommerce.service;

import com.iternova.ecommerce.exception.ProductException;
import com.iternova.ecommerce.model.Product;
import com.iternova.ecommerce.model.Rating;
import com.iternova.ecommerce.model.User;
import com.iternova.ecommerce.repository.RatingRepository;
import com.iternova.ecommerce.request.RatingRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingServiceImp implements RatingService{

    private RatingRepository ratingRepository;
    private ProductService productService;

    public RatingServiceImp(RatingRepository ratingRepository, ProductService productService){
        this.productService=productService;
        this.ratingRepository=ratingRepository;
    }
    @Override
    public Rating createRating(RatingRequest req, User user) throws ProductException {
       Product product = productService.findProductById(req.getProductId());
       Rating rating = new Rating();
       rating.setProduct(product);
       rating.setUser(user);
       rating.setRating(req.getRating());
       rating.setCreatedAt(LocalDateTime.now());

       return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getProductsRating(Long productId) {
        return ratingRepository.getAllProductsRating(productId);
    }
}
