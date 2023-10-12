package com.iternova.ecommerce.controller;

import com.iternova.ecommerce.exception.OrderException;
import com.iternova.ecommerce.exception.UserException;
import com.iternova.ecommerce.model.Address;
import com.iternova.ecommerce.model.Order;
import com.iternova.ecommerce.model.User;
import com.iternova.ecommerce.service.OrderService;
import com.iternova.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    public ResponseEntity<Order> createOrder(@RequestBody Address shippingAddres, @RequestHeader("Authorization") String jwt) throws UserException{
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.createOrder(user, shippingAddres);

        System.out.println("order " + order);

        return new ResponseEntity<Order>(order, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> usersOrderHistory(@RequestHeader("Authorization") String jwt) throws UserException{
        User user = userService.findUserProfileByJwt(jwt);
        List<Order> orders= orderService.usersOrderHistory(user.getId());

        return new ResponseEntity<>(orders,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findOrderById(
            @PathVariable("id") Long orderId, @RequestHeader("Authorization") String jwt) throws UserException, OrderException{
        User user = userService.findUserProfileByJwt(jwt);

        Order order = orderService.findOrderById(orderId);

        return new ResponseEntity<>(order,HttpStatus.CREATED);
    }


}
