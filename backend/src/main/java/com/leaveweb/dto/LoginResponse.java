package com.leaveweb.dto;

public record LoginResponse(String token, UserResponse user) {}