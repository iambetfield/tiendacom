package com.iternova.ecommerce.service;

import com.iternova.ecommerce.exception.CartItemException;
import com.iternova.ecommerce.exception.UserException;
import com.iternova.ecommerce.model.Cart;
import com.iternova.ecommerce.model.CartItem;
import com.iternova.ecommerce.model.Product;
import com.iternova.ecommerce.model.User;
import com.iternova.ecommerce.repository.CartItemRepository;
import com.iternova.ecommerce.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemServiceImp implements CartItemService{

    private CartItemRepository cartItemRepository;
    private UserService userService;
    private CartRepository cartRepository;

    public CartItemServiceImp(
            CartItemRepository cartItemRepository,
            UserService userService,
            CartRepository cartRepository
    ){
        this.cartItemRepository=cartItemRepository;
        this.cartRepository=cartRepository;
        this.userService=userService;
    }



    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice()*cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice()*cartItem.getQuantity());

        CartItem createdCartItem = cartItemRepository.save(cartItem);

        return createdCartItem;
    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {

        CartItem item = findCartItemById(id);
        User user = userService.findUserById(item.getUserId());

        if(user.getId().equals(userId)){
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(item.getQuantity() * item.getPrice());
            item.setDiscountedPrice(item.getProduct().getDiscountedPrice()* item.getQuantity());
        }
        return cartItemRepository.save(item);
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {
       CartItem cartItem = cartItemRepository.isCartItemExist(cart,product,size,userId);
       return cartItem;
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemid) throws CartItemException, UserException {
        CartItem cartItem = findCartItemById(cartItemid);

        User user = userService.findUserById(cartItem.getUserId());

        User reqUser = userService.findUserById(userId);

        if(user.getId().equals(reqUser.getId())){
            cartItemRepository.deleteById(cartItemid);
        }
        else {
            throw new UserException("you can't remove another users item");
        }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        Optional<CartItem> opt = cartItemRepository.findById(cartItemId);

        if(opt.isPresent()){
            return opt.get();
        }
        throw new CartItemException("CartItem not found with id "+ cartItemId);



    }
}
