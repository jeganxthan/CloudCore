package com.example.cloudcore.auth.controller;

import com.example.cloudcore.auth.dto.LoginRequest;
import com.example.cloudcore.auth.dto.LoginResponse;
import com.example.cloudcore.auth.dto.RegisterRequest;
import com.example.cloudcore.auth.dto.UserResponse;
import com.example.cloudcore.auth.service.AuthService;
import com.example.cloudcore.config.security.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserResponse register(
            @Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @RestController
    @RequestMapping("/test")
    @RequiredArgsConstructor
    public class TestController {

        private final JwtService jwtService;

        @GetMapping
        public String test(
                @RequestParam String token) {

            return jwtService.extractEmail(token);
        }
    }

}