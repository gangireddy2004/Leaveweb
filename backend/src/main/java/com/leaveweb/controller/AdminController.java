package com.leaveweb.controller;

import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.leaveweb.dto.LeaveRejectionRequest;
import com.leaveweb.dto.AccountRejectionRequest;
import com.leaveweb.dto.LeaveResponse;
import com.leaveweb.dto.UserResponse;
import com.leaveweb.service.AdminService;
import com.leaveweb.service.DashboardService;
import com.leaveweb.service.LeaveService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final LeaveService leaveService; private final AdminService adminService; private final DashboardService dashboardService;
    public AdminController(LeaveService leaveService, AdminService adminService, DashboardService dashboardService) { this.leaveService = leaveService; this.adminService = adminService; this.dashboardService = dashboardService; }
    @GetMapping("/dashboard") public Map<String, Object> dashboard() { return dashboardService.admin(); }
    @GetMapping("/users") public List<UserResponse> users() { return adminService.users(); }
    @GetMapping("/users/pending") public List<UserResponse> pendingUsers() { return adminService.pendingUsers(); }
    @PostMapping("/users/{id}/approve") public UserResponse approveUser(@PathVariable String id) { return adminService.approveUser(id); }
    @PostMapping("/users/{id}/reject") public UserResponse rejectUser(@PathVariable String id, @RequestBody(required = false) AccountRejectionRequest request) { return adminService.rejectUser(id, request == null ? null : request.reason()); }
    @GetMapping("/leaves") public List<LeaveResponse> leaves() { return leaveService.getAll(); }
    @PostMapping("/leaves/{id}/approve") public LeaveResponse approve(Authentication auth, @PathVariable String id) { return leaveService.approve(auth.getName(), id); }
    @PostMapping("/leaves/{id}/reject") public LeaveResponse reject(Authentication auth, @PathVariable String id, @Valid @RequestBody LeaveRejectionRequest request) { return leaveService.reject(auth.getName(), id, request.reason()); }
}