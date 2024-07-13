package com.yourengineerbro.AuthAPI.util;

// JwtUtil.java
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secretKey);
    }

    public String generateToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(getAlgorithm());
    }

    public boolean validateToken(String token, HttpServletResponse response) throws IOException {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm()).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            // can be specifically throw TokenExpiredException?
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid Token");
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(getAlgorithm()).build().verify(token);
        return decodedJWT.getSubject();
    }
}
