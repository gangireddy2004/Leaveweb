package com.leaveweb.dto;

import java.time.Instant;
import java.time.LocalDate;
import com.leaveweb.model.LeaveStatus;

public record LeaveResponse(String id, String userId, String employeeName, String employeeEmail, String leaveTypeId,
                            String leaveTypeName, LocalDate startDate, LocalDate endDate, long numberOfDays,
                            String reason, String supportingDocument, LeaveStatus status, Instant appliedAt,
                            Instant reviewedAt, String reviewedBy, String rejectionReason) {}