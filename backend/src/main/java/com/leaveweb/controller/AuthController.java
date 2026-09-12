package com.leaveweb.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.leaveweb.dto.LoginRequest;
import com.leaveweb.dto.LoginResponse;
import com.leaveweb.dto.RegisterRequest;
import com.leaveweb.dto.UserResponse;
import com.leaveweb.service.AuthService;
import com.leaveweb.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService; private final UserService userService;
    public AuthController(AuthService authService, UserService userService) { this.authService = authService; this.userService = userService; }
    @PostMapping("/register") public LoginResponse register(@Valid @RequestBody RegisterRequest request) { return authService.register(request); }
    @PostMapping("/login") public LoginResponse login(@Valid @RequestBody LoginRequest request) { return authService.login(request); }
    @GetMapping("/me") public UserResponse me(Authentication authentication) { return userService.toResponse(userService.getByEmail(authentication.getName())); }
}