package com.yourengineerbro.AuthAPI.controller;

// ProtectedController.java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected")
public class ProtectedController {

    @GetMapping
    public String getProtectedResource() {

        return "This is a protected resource.";
    }
}
