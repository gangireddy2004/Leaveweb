package com.leaveweb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@NotBlank String employeeId, @NotBlank String fullName, @NotBlank @Email String email,
                              @NotBlank @Size(min = 8, message = "Password must be at least 8 characters") String password,
                              String phone, @NotBlank String department) {}