package com.yourengineerbro.AuthAPI.controller;

// UserController.java

import com.yourengineerbro.AuthAPI.entity.User;
import com.yourengineerbro.AuthAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.yourengineerbro.AuthAPI.util.Constants.Path.AUTH_PATH;
import static com.yourengineerbro.AuthAPI.util.Constants.Path.SIGNUP_USER;

@RestController
@RequestMapping(AUTH_PATH)
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(SIGNUP_USER)
    public User signUp(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }
}
