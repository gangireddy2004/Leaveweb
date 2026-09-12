package com.leaveweb.dto;

import java.util.Map;
import jakarta.validation.constraints.NotEmpty;

public record ProfileChangeRequestDto(@NotEmpty Map<String, String> requestedChanges) {}