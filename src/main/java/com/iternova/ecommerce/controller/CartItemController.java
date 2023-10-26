package com.iternova.ecommerce.controller;

import com.iternova.ecommerce.exception.CartItemException;
import com.iternova.ecommerce.exception.UserException;
import com.iternova.ecommerce.model.CartItem;
import com.iternova.ecommerce.model.User;
import com.iternova.ecommerce.response.ApiResponse;
import com.iternova.ecommerce.service.CartItemService;
import com.iternova.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart_items")
public class CartItemController {
    @Autowired
    private UserService userService;
    @Autowired
    private CartItemService cartItemService;
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem (@PathVariable Long cartItemId, @RequestHeader("Authorization") String jwt) throws UserException, CartItemException{
        User user = userService.findUserProfileByJwt(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        ApiResponse res = new ApiResponse();
        res.setMessage("item added to cart");
        res.setStatus(true);

        System.out.println("Elemento eliminado en forma exitosa");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(
            @RequestBody CartItem cartItem,
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {

        User user = userService.findUserProfileByJwt(jwt);
        CartItem updateCartItem = cartItemService.updateCartItem(user.getId(), cartItemId,cartItem);
        System.out.println("Elemento modificado en forma exitosa");
        return new ResponseEntity<>(updateCartItem,HttpStatus.OK);
    }
}
