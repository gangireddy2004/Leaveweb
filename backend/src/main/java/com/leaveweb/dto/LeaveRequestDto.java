package com.leaveweb.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LeaveRequestDto(@NotBlank String leaveTypeId, @NotNull @FutureOrPresent LocalDate startDate,
                              @NotNull @FutureOrPresent LocalDate endDate, @NotBlank String reason,
                              String supportingDocument) {}