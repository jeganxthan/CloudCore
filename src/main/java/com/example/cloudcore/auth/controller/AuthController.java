package com.example.cloudcore.auth.controller;

import com.example.cloudcore.auth.dto.AccessTokenResponse;
import com.example.cloudcore.auth.dto.LoginRequest;
import com.example.cloudcore.auth.dto.RefreshRequest;
import com.example.cloudcore.auth.dto.RegisterRequest;
import com.example.cloudcore.auth.dto.TokenResponse;
import com.example.cloudcore.auth.dto.UserResponse;
import com.example.cloudcore.auth.service.AuthService;

import com.example.cloudcore.auth.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;

    @PostMapping("/register")
    public UserResponse register(
            @Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public TokenResponse login(
            @Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public AccessTokenResponse refresh(
            @RequestBody RefreshRequest request) {
        return authService.refresh(request);
    }

    @PostMapping("/logout")
    public String logout(
            Authentication authentication) {

        refreshTokenService.delete(
                authentication.getName());

        return "Logged out";
    }

}