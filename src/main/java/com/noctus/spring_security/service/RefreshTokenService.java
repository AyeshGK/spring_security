package com.noctus.spring_security.service;

import com.noctus.spring_security.config.jwt.JwtAuthenticationFilter;
import com.noctus.spring_security.exception.InvalidRefreshTokenException;
import com.noctus.spring_security.model.Token;
import com.noctus.spring_security.model.User;
import com.noctus.spring_security.repository.TokenRepository;
import com.noctus.spring_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24L; // 1 day
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private Token saveOrUpdateToken(User user, Token token) {
        Token refreshToken = tokenRepository.findByUser(user);
        if (refreshToken == null) {
            return tokenRepository.save(token);
        } else {
            refreshToken.setToken(token.getToken());
            refreshToken.setExpiryDate(token.getExpiryDate());
            return tokenRepository.save(refreshToken);
        }
    }

    public Token generateRefreshToken(User user) {

        Token refreshToken = Token.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plusMillis(REFRESH_TOKEN_EXPIRATION_TIME))
                .build();

        return saveOrUpdateToken(user, refreshToken);
    }

//    public Token verifyToken(Token token) throws InvalidRefreshTokenException {
//        if (token.getExpiryDate().isBefore(Instant.now())) {
//            tokenRepository.delete(token);
//            logger.info("Token expired; token deleted.");
//            throw new InvalidRefreshTokenException("Token expired");
//        }
//        return token;
//    }

    public boolean verifyToken(Token token) {
        return token.getExpiryDate().isAfter(Instant.now());
    }

    public Optional<Token> getRefreshToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public User getUserByToken(String token) {
        return tokenRepository.findByToken(token)
                .map(Token::getUser)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
    }
}
