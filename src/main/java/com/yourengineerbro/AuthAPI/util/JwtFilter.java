package com.yourengineerbro.AuthAPI.util;

import com.yourengineerbro.AuthAPI.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.yourengineerbro.AuthAPI.util.Constants.Exception.INVALID_TOKEN;
import static com.yourengineerbro.AuthAPI.util.Constants.Messages.INVALID_AUTH_HEADER;
import static com.yourengineerbro.AuthAPI.util.Constants.Messages.USER_NOT_FOUND;
import static com.yourengineerbro.AuthAPI.util.Constants.Path.*;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final List<String> EXCLUDED_URLS = List.of(AUTH_PATH + SIGNUP_USER,
            AUTH_PATH + SIGNIN_USER,
            "/swagger-ui/index.html");

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        if (EXCLUDED_URLS.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(INVALID_AUTH_HEADER);
            return;
        }

        String token = authorizationHeader.substring(7);

        if (!jwtUtil.validateToken(token, response)) {
            return;
        }
        if (tokenService.isTokenInvalidated(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(INVALID_TOKEN);
            return;
        }

        String email = jwtUtil.getEmailFromToken(token);
        if (email == null || email.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(INVALID_TOKEN);
        }
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (userDetails == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(USER_NOT_FOUND);
                return;
            }
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        chain.doFilter(request, response);
    }
}
