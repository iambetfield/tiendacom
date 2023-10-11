package com.iternova.ecommerce.service;

import com.iternova.ecommerce.exception.CartItemException;
import com.iternova.ecommerce.exception.UserException;
import com.iternova.ecommerce.model.Cart;
import com.iternova.ecommerce.model.CartItem;
import com.iternova.ecommerce.model.Product;

public interface CartItemService {

    public CartItem createCartItem(CartItem cartItem);

    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;

    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId);

    public void removeCartItem(Long userId, Long cartItemid) throws CartItemException, UserException;

    public CartItem findCartItemById(Long cartItemId) throws CartItemException;
}
