package com.noctus.spring_security.repository;

import com.noctus.spring_security.model.Token;
import com.noctus.spring_security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    Token findByUser(User user);

}
