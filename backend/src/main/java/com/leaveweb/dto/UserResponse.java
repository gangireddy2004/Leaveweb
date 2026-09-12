package com.leaveweb.dto;

import java.time.Instant;
import com.leaveweb.model.Role;
import com.leaveweb.model.AccountStatus;

public record UserResponse(String id, String employeeId, String fullName, String email, String phone,
                           String department, Role role, AccountStatus accountStatus, boolean active, Instant createdAt) {
    public UserResponse(String id, String employeeId, String fullName, String email, String phone,
                        String department, Role role, boolean active, Instant createdAt) {
        this(id, employeeId, fullName, email, phone, department, role, AccountStatus.APPROVED, active, createdAt);
    }
}