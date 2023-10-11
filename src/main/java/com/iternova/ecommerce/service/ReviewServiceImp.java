package com.iternova.ecommerce.service;

import com.iternova.ecommerce.exception.ProductException;
import com.iternova.ecommerce.model.Product;
import com.iternova.ecommerce.model.Review;
import com.iternova.ecommerce.model.User;
import com.iternova.ecommerce.repository.ProductRepository;
import com.iternova.ecommerce.repository.ReviewRepository;
import com.iternova.ecommerce.request.ReviewRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ReviewServiceImp implements ReviewService{

    private ReviewRepository reviewRepository;
    private ProductService productService;
    private ProductRepository productRepository;

    public ReviewServiceImp(ReviewRepository reviewRepository, ProductService productService, ProductRepository productRepository){
        this.productRepository=productRepository;
        this.productService=productService;
        this.reviewRepository=reviewRepository;
    }

    @Override
    public Review createReview(ReviewRequest req, User user) throws ProductException {

        Product product = productService.findProductById(req.getProductId());
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReview(req.getReview());
        review.setCreatedAt(LocalDateTime.now());

        productRepository.save(product);

        return null;
    }

    @Override
    public List<Review> getAllReviews(Long productId) {
        return reviewRepository.getAllProductsReview(productId);
    }
}
