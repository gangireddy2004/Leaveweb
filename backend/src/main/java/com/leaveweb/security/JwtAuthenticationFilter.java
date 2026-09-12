package com.leaveweb.security;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.leaveweb.service.UserService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService; private final UserService userService;
    public JwtAuthenticationFilter(JwtService jwtService, UserService userService) { this.jwtService = jwtService; this.userService = userService; }
    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication() == null) {
            try { String token = header.substring(7); String username = jwtService.extractUsername(token); UserDetails details = userService.loadUserByUsername(username); if (jwtService.isValid(token, details)) { var auth = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities()); auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); SecurityContextHolder.getContext().setAuthentication(auth); } } catch (RuntimeException ignored) { }
        }
        chain.doFilter(request, response);
    }
}