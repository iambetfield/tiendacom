package com.iternova.ecommerce.service;

import com.iternova.ecommerce.exception.OrderException;
import com.iternova.ecommerce.model.Address;
import com.iternova.ecommerce.model.Order;
import com.iternova.ecommerce.model.User;
import com.iternova.ecommerce.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImp implements OrderService{

    private CartRepository cartRepository;
    private CartService cartItemService;
    private ProductService productService;

    public OrderServiceImp(CartRepository cartRepository, CartService cartItemService, ProductService productService){
        this.cartItemService = cartItemService;
        this.productService = productService;
        this.cartRepository = cartRepository;
    }

    @Override
    public Order createOrder(User user, Address shippingAddress) {
        return null;
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return null;
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order confirmedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order canceledOrder(Long orderId) throws OrderException {
        return null;
    }
}
