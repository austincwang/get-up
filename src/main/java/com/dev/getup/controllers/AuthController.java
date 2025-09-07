package com.dev.getup.controllers;

import com.dev.getup.domain.dtos.AuthResponse;
import com.dev.getup.domain.dtos.LoginRequest;
import com.dev.getup.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authService;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        final UserDetails user = authService.authenticate(
                req.getEmail(),
                req.getPassword()
        );
        final String token = authService.generateToken(user);
        final AuthResponse resp = AuthResponse.builder()
                .token(token)
                .expiresIn(86400) // 24 hrs
                .build();

        return ResponseEntity.ok(resp);
    }
}
