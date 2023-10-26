package com.iternova.ecommerce.service;

import com.iternova.ecommerce.exception.OrderException;
import com.iternova.ecommerce.model.*;
import com.iternova.ecommerce.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImp implements OrderService{

    private CartService cartService; //2

    private OrderItemRepository orderItemRepository; //6

    private OrderRepository orderRepository; //1

    private OrderItemService orderItemService; //5

    private AddressRepository addressRepository;//3
    private UserRepository userRepository; //4


    public OrderServiceImp(CartService cartService, OrderItemRepository orderItemRepository, OrderRepository orderRepository, OrderItemService orderItemService, AddressRepository addressRepository, UserRepository userRepository) {
        this.cartService = cartService;
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Order createOrder(User user, Address shippingAddress) {
        System.out.println("Entramos en el servicio para CREAR LA ORDEN");
        shippingAddress.setUser(user);
        Address address = addressRepository.save(shippingAddress);
        user.getAddress().add(address);
        userRepository.save(user);

        Cart cart = cartService.findUserCart(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem item : cart.getCartItems()){
            OrderItem orderItem = new OrderItem();


            orderItem.setPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setSize(item.getSize());
            orderItem.setUserId(item.getUserId());
            orderItem.setDiscountedPrice(item.getDiscountedPrice());

            OrderItem createdOrderItem = orderItemRepository.save(orderItem);

            orderItems.add(createdOrderItem);
        }

        Order createdOrder = new Order();
        createdOrder.setUser(user);
        createdOrder.setOrderItems(orderItems);
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        createdOrder.setDiscount(cart.getDiscounted());
        createdOrder.setTotalItem(cart.getTotalItem());

        createdOrder.setShippingAddress(address);
        createdOrder.setOrderDate(LocalDateTime.now());
        createdOrder.setOrderStatus("PENDING");
        createdOrder.getPaymentDetails().setStatus("PENDING");
        createdOrder.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(createdOrder);

        // Crear una copia de la lista orderItems
        List<OrderItem> orderItemsCopy = new ArrayList<>(orderItems);

        // Iterar sobre la copia
        for (OrderItem item : orderItemsCopy) {
            item.setOrder(savedOrder);

            orderItemRepository.save(item);
        }
        System.out.println("orden creada con EXITO");
        return savedOrder;
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
        Optional<Order> optional = orderRepository.findById(orderId);
        if(optional.isPresent()){
            return optional.get();
        }
        throw new OrderException("order not exist with id"+ orderId);
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return null;
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {

        Order order = findOrderById(orderId);
        order.setOrderStatus("PLACED");
        order.getPaymentDetails().setStatus("COMPLETED");
        return order;
    }

    @Override
    public Order confirmedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("CONFIRMED");
        return orderRepository.save(order);
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("SHIPPED");
        return orderRepository.save(order);
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("DELIVERED");
        return orderRepository.save(order);
    }

    @Override
    public Order canceledOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("CANCELLED");
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<Order> getAllOrders() {

        return null;
    }


}
