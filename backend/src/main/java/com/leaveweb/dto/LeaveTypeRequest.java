package com.leaveweb.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record LeaveTypeRequest(@NotBlank String name, String description, @Min(1) int annualAllowance, boolean active) {}