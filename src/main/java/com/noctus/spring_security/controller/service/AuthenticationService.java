package com.noctus.spring_security.controller.service;

import com.noctus.spring_security.config.jwt.JwtAuthenticationFilter;
import com.noctus.spring_security.config.service.JwtService;
import com.noctus.spring_security.exception.UserAlreadyExistsException;
import com.noctus.spring_security.model.Role;
import com.noctus.spring_security.model.User;
import com.noctus.spring_security.dto.request.SignInRequest;
import com.noctus.spring_security.dto.request.SignUpRequest;
import com.noctus.spring_security.dto.response.AuthResponse;
import com.noctus.spring_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public AuthResponse register(SignUpRequest request) throws UserAlreadyExistsException {
//        find user by email if exists then return error
        if (repository.existsByEmail(request.getEmail())) {
            logger.error("User already exists");
            throw new UserAlreadyExistsException("User already exists.");
        }

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse login(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
