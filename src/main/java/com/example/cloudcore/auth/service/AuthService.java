package com.example.cloudcore.auth.service;

import com.example.cloudcore.auth.dto.LoginRequest;
import com.example.cloudcore.auth.dto.RegisterRequest;
import com.example.cloudcore.auth.dto.UserResponse;
import com.example.cloudcore.common.exception.BadRequestException;
import com.example.cloudcore.config.security.JwtService;
import com.example.cloudcore.entity.User;
import com.example.cloudcore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.cloudcore.auth.dto.LoginResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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

    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new BadRequestException("User not found"));

        boolean matches = passwordEncoder.matches(
                request.password(),
                user.getPassword());

        if (!matches) {
            throw new BadRequestException("Invalid credentials");
        }

        String token = jwtService.generateToken(
                user.getEmail());

        return new LoginResponse(token);
    }
}