package com.leaveweb.controller;

import java.util.List;
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
import com.leaveweb.dto.LeaveTypeRequest;
import com.leaveweb.model.LeaveType;
import com.leaveweb.service.LeaveTypeService;

@RestController
@RequestMapping({"/api/leave-types", "/api/admin/leave-types"})
public class LeaveTypeController {
    private final LeaveTypeService service;
    public LeaveTypeController(LeaveTypeService service) { this.service = service; }
    @GetMapping public List<LeaveType> list() { return service.getAll(true); }
    @GetMapping("/{id}") public LeaveType get(@PathVariable String id) { return service.get(id); }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") public LeaveType create(@Valid @RequestBody LeaveTypeRequest request) { return service.create(request); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public LeaveType update(@PathVariable String id, @Valid @RequestBody LeaveTypeRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public void delete(@PathVariable String id) { service.delete(id); }
}