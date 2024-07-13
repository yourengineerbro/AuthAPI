package com.yourengineerbro.AuthAPI.controller;

// UserController.java
import com.yourengineerbro.AuthAPI.entity.User;
import com.yourengineerbro.AuthAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public User signUp(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }
}
