package com.iternova.ecommerce.controller;

import com.iternova.ecommerce.exception.OrderException;
import com.iternova.ecommerce.exception.UserException;
import com.iternova.ecommerce.model.Order;
import com.iternova.ecommerce.model.User;
import com.iternova.ecommerce.repository.OrderRepository;
import com.iternova.ecommerce.response.ApiResponse;
import com.iternova.ecommerce.response.PaymentLinkResponse;
import com.iternova.ecommerce.service.OrderService;
import com.iternova.ecommerce.service.UserService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Value("${razorpay.api.key}")
    String apiKey;

    @Value("${razorpay.api.secret}")
    String apiSecret;
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/payments/{orderId}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws UserException, OrderException, RazorpayException {
       // User user = userService.findUserProfileByJwt(jwt);

        Order order = orderService.findOrderById(orderId);
        try {
            RazorpayClient razorpay = new RazorpayClient(apiKey,apiSecret);

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", order.getTotalPrice()*100);
            paymentLinkRequest.put("currency", "USD");

            JSONObject customer = new JSONObject();
            customer.put("name", order.getUser().getFirstName());
            customer.put("email", order.getUser().getEmail());
            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("callback_url", "http://localhost/5173"+orderId);
            paymentLinkRequest.put("callback_method", "get");

            PaymentLink paymentLink = razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = paymentLink.get("id");
            String paymentLinkUrl = paymentLink.get("short_url");

            PaymentLinkResponse res = new PaymentLinkResponse();
            res.setPayment_link_id(paymentLinkId);
            res.setPayment_link_url(paymentLinkUrl);

            return new ResponseEntity<PaymentLinkResponse>(res, HttpStatus.CREATED);
        } catch (RazorpayException e) {
            throw new RazorpayException(e.getMessage());
        }
    }

    @GetMapping("/payments")
    public ResponseEntity<ApiResponse> redirect(
            @RequestParam(name =" payment_id") String paymentId,
            @RequestParam(name="order_id") Long orderId) throws OrderException, RazorpayException {

        Order order = orderService.findOrderById(orderId);
        RazorpayClient razorpayClient = new RazorpayClient(apiKey,apiSecret);


        try {
            Payment payment = razorpayClient.payments.fetch(paymentId);

            if(payment.get("status").equals("captured")){
                order.getPaymentDetails().setPaymentId(paymentId);
                order.getPaymentDetails().setStatus("COMPLETED");
                order.setOrderStatus("PLACED");
                orderRepository.save(order);
            }

            ApiResponse res = new ApiResponse();
            res.setMessage("Your order get placed");
            res.setStatus(true);

            return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);

        }catch (Exception e){
            throw new RazorpayException(e.getMessage());
        }
    }
}
