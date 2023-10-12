package com.iternova.ecommerce.service;

import com.iternova.ecommerce.exception.OrderException;
import com.iternova.ecommerce.model.Address;
import com.iternova.ecommerce.model.Order;
import com.iternova.ecommerce.model.User;


import java.util.List;

public interface OrderService{

    public Order createOrder(User user, Address shippingAddress);

    public Order findOrderById(Long orderId) throws OrderException;

    public List<Order> usersOrderHistory(Long userId);

    public Order placedOrder(Long orderId) throws OrderException;
    public Order confirmedOrder(Long orderId) throws  OrderException;
    public Order shippedOrder(Long orderId) throws OrderException;
    public Order deliveredOrder(Long orderId) throws OrderException;
    public Order canceledOrder(Long orderId) throws OrderException;

    public void deleteOrder(Long orderId) throws OrderException;
    List<Order> getAllOrders();
}
