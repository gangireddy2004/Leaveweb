package com.leaveweb.dto;

import java.time.Instant;
import java.util.Map;
import com.leaveweb.model.ProfileChangeStatus;

public record ProfileChangeResponse(String id, String userId, UserResponse user, Map<String, String> requestedChanges, ProfileChangeStatus status, Instant requestedAt, Instant reviewedAt, String reviewedBy, String rejectionReason) {}