package com.iternova.ecommerce.controller;

import com.iternova.ecommerce.exception.UserException;
import com.iternova.ecommerce.model.User;
import com.iternova.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String jwt) throws UserException{

        User user = userService.findUserProfileByJwt(jwt);
        System.out.println("usuario encontrado");
        return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
    }
}
