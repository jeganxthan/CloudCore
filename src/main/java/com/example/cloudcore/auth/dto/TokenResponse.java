package com.example.cloudcore.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {}