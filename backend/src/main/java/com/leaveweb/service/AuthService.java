package com.leaveweb.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.leaveweb.dto.LoginRequest;
import com.leaveweb.dto.LoginResponse;
import com.leaveweb.dto.RegisterRequest;
import com.leaveweb.security.JwtService;
import com.leaveweb.model.AccountStatus;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager; private final UserService userService; private final JwtService jwtService;
    public AuthService(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService) { this.authenticationManager = authenticationManager; this.userService = userService; this.jwtService = jwtService; }
    public LoginResponse login(LoginRequest request) { var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password())); UserDetails details = (UserDetails) authentication.getPrincipal(); return new LoginResponse(jwtService.generateToken(details), userService.toResponse(userService.getByEmail(details.getUsername()))); }
    public LoginResponse register(RegisterRequest request) { var user = userService.register(request); if (user.getAccountStatus() != AccountStatus.APPROVED) return new LoginResponse(null, userService.toResponse(user)); UserDetails details = userService.loadUserByUsername(user.getEmail()); return new LoginResponse(jwtService.generateToken(details), userService.toResponse(user)); }
}