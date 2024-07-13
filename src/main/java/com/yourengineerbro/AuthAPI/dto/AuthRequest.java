package com.yourengineerbro.AuthAPI.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

// AuthRequest.java
@Getter
@Setter
@Component
public class AuthRequest {
    private String email;
    private String password;

    // getters and setters
}
