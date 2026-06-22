package com.example.cloudcore.auth.dto;

public record UserResponse(
        Long id,
        String name,
        String email
) {}