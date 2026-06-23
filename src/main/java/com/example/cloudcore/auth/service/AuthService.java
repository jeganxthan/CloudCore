package com.example.cloudcore.auth.service;

import com.example.cloudcore.auth.dto.AccessTokenResponse;
import com.example.cloudcore.auth.dto.LoginRequest;
import com.example.cloudcore.auth.dto.RefreshRequest;
import com.example.cloudcore.auth.dto.RegisterRequest;
import com.example.cloudcore.auth.dto.TokenResponse;
import com.example.cloudcore.auth.dto.UserResponse;
import com.example.cloudcore.common.exception.BadRequestException;
import com.example.cloudcore.config.security.JwtService;
import com.example.cloudcore.entity.User;
import com.example.cloudcore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already exists");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail());
    }

    public TokenResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new BadRequestException("User not found"));

        boolean matches = passwordEncoder.matches(
                request.password(),
                user.getPassword());

        if (!matches) {
            throw new BadRequestException("Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(
                user.getEmail());

        String refreshToken = jwtService.generateRefreshToken(
                user.getEmail());

        return new TokenResponse(
                accessToken,
                refreshToken);
    }

    public AccessTokenResponse refresh(
            RefreshRequest request) {

        String refreshToken = request.refreshToken();

        if (!jwtService.isTokenValid(refreshToken)) {
            throw new BadRequestException(
                    "Invalid refresh token");
        }

        String email = jwtService.extractEmail(refreshToken);

        String storedToken = refreshTokenService.get(email);

        if (storedToken == null ||
                !storedToken.equals(refreshToken)) {

            throw new BadRequestException(
                    "Refresh token not found");
        }

        String accessToken = jwtService.generateAccessToken(email);

        return new AccessTokenResponse(
                accessToken);
    }
}