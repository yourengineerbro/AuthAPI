package com.yourengineerbro.AuthAPI.controller;

// AuthController.java

import com.yourengineerbro.AuthAPI.dto.AuthRequest;
import com.yourengineerbro.AuthAPI.util.JwtUtil;
import com.yourengineerbro.AuthAPI.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.yourengineerbro.AuthAPI.util.Constants.Exception.INVALID_CREDENTIALS;
import static com.yourengineerbro.AuthAPI.util.Constants.Exception.INVALID_TOKEN;
import static com.yourengineerbro.AuthAPI.util.Constants.Messages.SIGNED_OUT;
import static com.yourengineerbro.AuthAPI.util.Constants.Path.*;


@RestController
@RequestMapping(AUTH_PATH)
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenService tokenService;

    @PostMapping(REVOKE_TOKEN)
    public void revokeToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String token = request.getHeader("Authorization").substring(7);
        tokenService.invalidateToken(token);
        response.setStatus(HttpServletResponse.SC_OK);
        // Or message can be Token has been revoked.
        response.getWriter().write(SIGNED_OUT);
    }

    // AuthController.java
    @PostMapping(REFRESH_TOKEN)
    public String refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String token = request.getHeader("Authorization").substring(7);

        if (jwtUtil.validateToken(token, response)) {
            String email = jwtUtil.getEmailFromToken(token);
            tokenService.invalidateToken(token);
            return jwtUtil.generateToken(email);
        } else {
            throw new RuntimeException(INVALID_TOKEN);
        }
    }


    @PostMapping(SIGNIN_USER)
    public String signIn(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                            authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                return jwtUtil.generateToken(authRequest.getEmail());
            }
            return INVALID_CREDENTIALS;
        } catch (AuthenticationException e) {
            return INVALID_CREDENTIALS;
        }
    }
}
