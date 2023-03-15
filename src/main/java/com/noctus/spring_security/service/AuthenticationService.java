package com.noctus.spring_security.service;

import com.noctus.spring_security.config.jwt.JwtAuthenticationFilter;
import com.noctus.spring_security.dto.request.TokenRefreshRequest;
import com.noctus.spring_security.exception.InvalidRefreshTokenException;
import com.noctus.spring_security.exception.UserAlreadyExistsException;
import com.noctus.spring_security.model.ERole;
import com.noctus.spring_security.model.Role;
import com.noctus.spring_security.model.User;
import com.noctus.spring_security.dto.request.SignInRequest;
import com.noctus.spring_security.dto.request.SignUpRequest;
import com.noctus.spring_security.dto.response.AuthResponse;
import com.noctus.spring_security.repository.RoleRepository;
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
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    private final UserRepository repository;
    private final RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    public AuthResponse register(SignUpRequest request) throws UserAlreadyExistsException {
//        find user by email if exists then return error
        if (repository.existsByEmail(request.getEmail())) {
            logger.error("User already exists");
            throw new UserAlreadyExistsException("User already exists.");
        }

        Role role = roleRepository.findByName(ERole.USER.toString()).orElseThrow();

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.generateRefreshToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken.getToken())
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
        var refreshToken = refreshTokenService.generateRefreshToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AuthResponse refresh(TokenRefreshRequest request) throws InvalidRefreshTokenException {
        var token = refreshTokenService.getRefreshToken(request.getRefreshToken());
        if (token.isEmpty()) {
            logger.error("Invalid refresh token");
            throw new InvalidRefreshTokenException("Invalid refresh token.");
        }

        if (refreshTokenService.verifyToken(token.get())) {
            var user = refreshTokenService.getUserByToken(request.getRefreshToken());
            var jwtToken = jwtService.generateToken(user);
            return AuthResponse.builder()
                    .token(jwtToken)
                    .refreshToken(request.getRefreshToken())
                    .build();
        } else {
            logger.error("Invalid refresh token");
            throw new InvalidRefreshTokenException("Invalid refresh token.");
        }
    }
}
