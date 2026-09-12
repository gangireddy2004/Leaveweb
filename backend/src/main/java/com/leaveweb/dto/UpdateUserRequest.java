package com.leaveweb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(@NotBlank String fullName, @NotBlank @Email String email, String phone,
                                @NotBlank String department, Boolean active) {}