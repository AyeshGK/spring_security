package com.noctus.spring_security.controller;

import com.noctus.spring_security.dto.request.TokenRefreshRequest;
import com.noctus.spring_security.exception.InvalidRefreshTokenException;
import com.noctus.spring_security.exception.UserAlreadyExistsException;
import com.noctus.spring_security.dto.request.SignInRequest;
import com.noctus.spring_security.dto.request.SignUpRequest;
import com.noctus.spring_security.dto.response.AuthResponse;
import com.noctus.spring_security.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService service;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignUpRequest request) throws UserAlreadyExistsException {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody SignInRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody TokenRefreshRequest request) throws InvalidRefreshTokenException {
        return ResponseEntity.ok(service.refresh(request));
    }
}
