package com.iternova.ecommerce.controller;

import com.iternova.ecommerce.exception.OrderException;
import com.iternova.ecommerce.model.Order;
import com.iternova.ecommerce.response.ApiReponse;
import com.iternova.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {


    private OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrdersHandler(){
        List<Order> orders= orderService.getAllOrders();

        return new ResponseEntity<List<Order>>(orders, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/confirmed")
    public ResponseEntity<Order> ConfirmedOrderHandler(@PathVariable Long orderId,
                                                       @RequestHeader("Authorization") String jwt) throws OrderException {
        Order order = orderService.confirmedOrder(orderId);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/ship")
    public ResponseEntity<Order> shippedOrderHandler(@PathVariable Long orderId,
                                                     @RequestHeader("Authorization") String jwt) throws OrderException{
        Order order = orderService.shippedOrder(orderId);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }


    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Order> deliverOrderHandler(@PathVariable Long orderId,
                                                     @RequestHeader("Authorization") String jwt) throws OrderException{
        Order order = orderService.deliveredOrder(orderId);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrderHandler(@PathVariable Long orderId,
                                                     @RequestHeader("Authorization") String jwt) throws OrderException{
        Order order = orderService.canceledOrder(orderId);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<ApiReponse> deleteOrderHandler(@PathVariable Long orderId,
                                                     @RequestHeader("Authorization") String jwt) throws OrderException{
        orderService.deleteOrder(orderId);

        ApiReponse res = new ApiReponse();
        res.setMessage("order deleted successfully");
        res.setStatus(true);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}


