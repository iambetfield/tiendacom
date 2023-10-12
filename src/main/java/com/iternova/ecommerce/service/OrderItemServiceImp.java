package com.iternova.ecommerce.service;

import com.iternova.ecommerce.model.OrderItem;
import com.iternova.ecommerce.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImp implements OrderItemService{
    @Autowired
    private OrderItemRepository orderItemRepository;


    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }
}
