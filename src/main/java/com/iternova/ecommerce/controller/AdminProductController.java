package com.iternova.ecommerce.controller;

import com.iternova.ecommerce.exception.ProductException;
import com.iternova.ecommerce.model.Product;
import com.iternova.ecommerce.request.CreateProductRequest;
import com.iternova.ecommerce.response.ApiReponse;
import com.iternova.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest req){
        Product product = productService.createProduct(req);
        return new ResponseEntity<Product>(product, HttpStatus.CREATED);
    }
    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiReponse>deleteProduct(@PathVariable Long productId) throws ProductException {
        productService.deleteProduct(productId);

        ApiReponse res = new ApiReponse();
        res.setMessage("order deleted successfully");
        res.setStatus(true);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> findAllProducts(){
        List<Product> products = productService.findAllproducts();

        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> updateProduct(@RequestBody Product req, @PathVariable Long productId) throws ProductException{
        Product product = productService.updateProduct(productId,req);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PostMapping("/creates")
    public ResponseEntity<ApiReponse> createMultipleProducts(@RequestBody CreateProductRequest[] req){
        for(CreateProductRequest product: req){
            productService.createProduct(product);
        }
        ApiReponse res = new ApiReponse();
        res.setMessage("product created successfully");
        res.setStatus(true);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}