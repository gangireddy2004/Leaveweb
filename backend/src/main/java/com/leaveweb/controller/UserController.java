package com.leaveweb.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import com.leaveweb.dto.UpdateUserRequest;
import com.leaveweb.dto.RegisterRequest;
import com.leaveweb.dto.UserResponse;
import com.leaveweb.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service = service; }
    @GetMapping("/me") public UserResponse me(Authentication auth) { return service.toResponse(service.getByEmail(auth.getName())); }
    @GetMapping @PreAuthorize("hasRole('ADMIN')") public java.util.List<UserResponse> list() { return service.getAll().stream().map(service::toResponse).toList(); }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") public UserResponse create(@Valid @RequestBody RegisterRequest request) { return service.toResponse(service.register(request)); }
    @GetMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public UserResponse get(@PathVariable String id) { return service.toResponse(service.getById(id)); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public UserResponse update(@PathVariable String id, @Valid @RequestBody UpdateUserRequest request) { return service.toResponse(service.update(id, request)); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public void delete(@PathVariable String id) { service.deactivate(id); }
}