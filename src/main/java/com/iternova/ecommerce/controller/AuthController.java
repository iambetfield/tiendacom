package com.iternova.ecommerce.controller;

import com.iternova.ecommerce.config.JwtProvider;
import com.iternova.ecommerce.exception.UserException;
import com.iternova.ecommerce.model.Cart;
import com.iternova.ecommerce.model.User;
import com.iternova.ecommerce.repository.UserRepository;
import com.iternova.ecommerce.request.LoginRequest;
import com.iternova.ecommerce.response.AuthResponse;
import com.iternova.ecommerce.service.CartService;
import com.iternova.ecommerce.service.CustomerUserServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserRepository  userRepository;
    private JwtProvider jwtProvider;
    private CustomerUserServiceImp customerUserServiceImp;
    private PasswordEncoder passwordEncoder;

    private CartService cartService;


    public AuthController(CartService cartService, UserRepository userRepository,JwtProvider jwtProvider , CustomerUserServiceImp customerUserServiceImp,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.customerUserServiceImp = customerUserServiceImp;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.cartService = cartService;

    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException{
        String email= user.getEmail();
        String password = user.getPassword();
        String firstName = user. getFirstName();
        String lastName= user.getLastName();

        User isEmailExist = userRepository.findByEmail(email);

        if(isEmailExist!=null){
            throw new UserException("Email is already used in another account");
        }
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setFirstName(firstName);
        createdUser.setLastName(lastName);

        User savedUser = userRepository.save(createdUser);
        Cart cart = cartService.createCart(savedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(),savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse(token,"Sign up Success!!");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);

    }
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest){
        String username= loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse(token,"Signin Success!!");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);

    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customerUserServiceImp.loadUserByUsername(username);

        if(userDetails==null){
            throw new BadCredentialsException("invalid username");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
    }
}
