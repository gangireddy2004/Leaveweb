package com.leaveweb.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.leaveweb.dto.*;
import com.leaveweb.service.ProfileChangeRequestService;

@RestController
public class ProfileChangeRequestController {
    private final ProfileChangeRequestService service;
    public ProfileChangeRequestController(ProfileChangeRequestService service) { this.service = service; }
    @PostMapping("/api/profile-change-requests") public ProfileChangeResponse create(Authentication auth, @Valid @RequestBody ProfileChangeRequestDto request) { return service.create(auth.getName(), request); }
    @GetMapping("/api/profile-change-requests/me") public List<ProfileChangeResponse> mine(Authentication auth) { return service.mine(auth.getName()); }
    @GetMapping("/api/admin/profile-change-requests") public List<ProfileChangeResponse> all() { return service.all(); }
    @PostMapping("/api/admin/profile-change-requests/{id}/approve") public ProfileChangeResponse approve(Authentication auth, @PathVariable String id) { return service.approve(id, auth.getName()); }
    @PostMapping("/api/admin/profile-change-requests/{id}/reject") public ProfileChangeResponse reject(Authentication auth, @PathVariable String id, @RequestBody(required = false) AccountRejectionRequest request) { return service.reject(id, auth.getName(), request == null ? null : request.reason()); }
}