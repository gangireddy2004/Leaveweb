package com.leaveweb.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.leaveweb.dto.UserResponse;
import com.leaveweb.model.User;

@Service
public class AdminService {
    private final UserService userService;
    private final EmailService emailService;
    public AdminService(UserService userService, EmailService emailService) { this.userService = userService; this.emailService = emailService; }
    public List<UserResponse> users() { return userService.getAll().stream().map(userService::toResponse).toList(); }
    public List<UserResponse> pendingUsers() { return userService.getPending().stream().map(userService::toResponse).toList(); }
    public UserResponse approveUser(String id) { UserResponse response = userService.toResponse(userService.approve(id)); emailService.sendAccountApprovedEmail(response.email()); return response; }
    public UserResponse rejectUser(String id, String reason) { UserResponse response = userService.toResponse(userService.reject(id, reason)); emailService.sendAccountRejectedEmail(response.email(), reason); return response; }
}