package com.leaveweb.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.leaveweb.dto.LeaveRequestDto;
import com.leaveweb.dto.LeaveResponse;
import com.leaveweb.service.LeaveService;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {
    private final LeaveService service;
    public LeaveController(LeaveService service) { this.service = service; }
    @PostMapping public LeaveResponse create(Authentication auth, @Valid @RequestBody LeaveRequestDto request) { return service.create(auth.getName(), request); }
    @GetMapping public List<LeaveResponse> list(Authentication auth) { return service.getForUser(auth.getName()); }
    @GetMapping("/{id}") public LeaveResponse get(Authentication auth, @PathVariable String id) { return service.getById(auth.getName(), id); }
    @PutMapping("/{id}") public LeaveResponse update(Authentication auth, @PathVariable String id, @Valid @RequestBody LeaveRequestDto request) { return service.update(auth.getName(), id, request); }
    @DeleteMapping("/{id}") public void delete(Authentication auth, @PathVariable String id) { service.delete(auth.getName(), id); }
    @PostMapping("/{id}/cancel") public LeaveResponse cancel(Authentication auth, @PathVariable String id) { return service.cancel(auth.getName(), id); }
}