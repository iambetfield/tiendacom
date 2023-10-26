package com.iternova.ecommerce.service;

import com.iternova.ecommerce.exception.ProductException;
import com.iternova.ecommerce.model.Product;
import com.iternova.ecommerce.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public Product createProduct(CreateProductRequest req);

    public String deleteProduct(Long producId) throws ProductException;

    public Product updateProduct(Long productId, Product product) throws ProductException;

    public Product findProductById(Long id) throws ProductException;

    public List<Product> findProductByategory(String category);

    public Page<Product> getAllProducts(String category, List<String> colors, List<String> sizes,
                                        Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock,
                                        Integer PageNumber, Integer pageSize);

    public List<Product> findAllproducts();

    public List<Product> findProductsByCategory(String category);
}
