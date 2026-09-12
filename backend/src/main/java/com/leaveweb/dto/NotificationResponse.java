package com.leaveweb.dto;

import java.time.Instant;
import com.leaveweb.model.NotificationType;

public record NotificationResponse(String id, String title, String message, NotificationType type, boolean read, Instant createdAt) {}