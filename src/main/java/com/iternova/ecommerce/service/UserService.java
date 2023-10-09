package com.iternova.ecommerce.service;

import com.iternova.ecommerce.exception.UserException;
import com.iternova.ecommerce.model.User;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.stereotype.Service;


public interface UserService {

    public User findUserById(Long userId) throws UserException;

    public User findUserProfileByJwt(String jwt) throws UserException;
}
