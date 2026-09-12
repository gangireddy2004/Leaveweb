package com.leaveweb.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import com.leaveweb.dto.LoginRequest;
import com.leaveweb.dto.LoginResponse;
import com.leaveweb.dto.UserResponse;
import com.leaveweb.model.Role;
import com.leaveweb.security.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock AuthenticationManager authenticationManager;
    @Mock UserService userService;
    @Mock JwtService jwtService;
    @InjectMocks AuthService authService;

    @Test void loginReturnsTokenAndUser() { User principal = (User) User.withUsername("employee@example.com").password("hash").roles("EMPLOYEE").build(); UserResponse response = new UserResponse("1", "EMP-1", "Test Employee", "employee@example.com", null, "Engineering", Role.EMPLOYEE, true, null); when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())); when(jwtService.generateToken(principal)).thenReturn("jwt-token"); when(userService.getByEmail(principal.getUsername())).thenReturn(new com.leaveweb.model.User()); when(userService.toResponse(any())).thenReturn(response); LoginResponse result = authService.login(new LoginRequest("employee@example.com", "password")); assertEquals("jwt-token", result.token()); assertEquals("employee@example.com", result.user().email()); }
}