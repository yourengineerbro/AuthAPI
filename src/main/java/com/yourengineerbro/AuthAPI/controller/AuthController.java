package com.yourengineerbro.AuthAPI.controller;

// AuthController.java
import com.yourengineerbro.AuthAPI.dto.AuthRequest;
import com.yourengineerbro.AuthAPI.util.JwtUtil;
import com.yourengineerbro.AuthAPI.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
//import java.net.http.HttpHeaders;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // AuthController.java
    @Autowired
    private TokenService tokenService;

    @PostMapping("/revoke")
    public void revokeToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getHeader("Authorization").substring(7);
        //        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
//        if(token == null || !token.startsWith("Bearer ")) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Authorization header missing or invalid");
//            return;
//        }
//        assert token != null;
//        token = token.substring(7);
        System.out.println("Revoking token called");
        System.out.println("token: " + token);
        tokenService.invalidateToken(token);


        response.setStatus(HttpServletResponse.SC_OK);
        // Or message can be Token has been revoked.
        response.getWriter().write("User Successfully Signed out");
    }

    // AuthController.java
    @PostMapping("/refresh")
    public String refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getHeader("Authorization").substring(7);
//        if(token == null || !token.startsWith("Bearer ")) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////            response.getWriter().write("Authorization header missing or invalid");
//            return "Authorization header missing or invalid";
//        }

        if (jwtUtil.validateToken(token, response)) {
            String email = jwtUtil.getEmailFromToken(token);
            tokenService.invalidateToken(token);
            return jwtUtil.generateToken(email);
        } else {
            throw new RuntimeException("Invalid Token");
        }
    }


    @PostMapping("/signin")
    public String signIn(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            if(authentication.isAuthenticated()) {
                return jwtUtil.generateToken(authRequest.getEmail());
            }
            return "Invalid credentials";
        } catch (AuthenticationException e) {
            return "Invalid credentials";
//            throw new RuntimeException("Invalid credentials");
        }
//        return "Invalid credentials";
//        return "";
    }
}
