package com.iternova.ecommerce.service;

import com.iternova.ecommerce.exception.ProductException;
import com.iternova.ecommerce.model.Cart;
import com.iternova.ecommerce.model.User;
import com.iternova.ecommerce.request.AddItemRequest;

public interface CartService {

    public Cart createCart(User user);

    public String addCartItem(Long userId, AddItemRequest req) throws ProductException;
    public Cart findUserCart(Long userId);
}
