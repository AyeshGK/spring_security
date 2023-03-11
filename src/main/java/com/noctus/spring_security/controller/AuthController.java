package com.noctus.spring_security.controller;

import com.noctus.spring_security.exception.UserAlreadyExistsException;
import com.noctus.spring_security.payload.request.SignInRequest;
import com.noctus.spring_security.payload.request.SignUpRequest;
import com.noctus.spring_security.payload.response.AuthResponse;
import com.noctus.spring_security.controller.service.AuthenticationService;
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
}
