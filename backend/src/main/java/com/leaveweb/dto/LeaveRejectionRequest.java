package com.leaveweb.dto;

import jakarta.validation.constraints.NotBlank;

public record LeaveRejectionRequest(@NotBlank String reason) {}