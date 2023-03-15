package com.noctus.spring_security.seed;

import com.noctus.spring_security.config.jwt.JwtAuthenticationFilter;
import com.noctus.spring_security.model.ERole;
import com.noctus.spring_security.model.Role;
import com.noctus.spring_security.model.User;
import com.noctus.spring_security.repository.RoleRepository;
import com.noctus.spring_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    public void run(String... args) throws Exception {
        loadRoles();
        loadUsers();
    }

    private void loadRoles() {
        var role_1 = Role.builder()
                .name(ERole.SUPER_ADMIN.toString())
                .build();

        var role_2 = Role.builder()
                .name(ERole.ADMIN.toString())
                .build();

        var role_3 = Role.builder()
                .name(ERole.MODERATOR.toString())
                .build();

        var role_4 = Role.builder()
                .name(ERole.USER.toString())
                .build();

        roleRepository.saveAll(List.of(role_1, role_2, role_3, role_4));

    }

    public void loadUsers() {
        Role role_1 = roleRepository.findByName(ERole.USER.toString()).orElseThrow();
        Role role_2 = roleRepository.findByName(ERole.ADMIN.toString()).orElseThrow();
        Role role_3 = roleRepository.findByName(ERole.MODERATOR.toString()).orElseThrow();
        Role role_4 = roleRepository.findByName(ERole.SUPER_ADMIN.toString()).orElseThrow();

        var user_1 = User.builder()
                .firstname("sadmin")
                .lastname("sadmin")
                .email("sadmin@gmail.com")
                .password(passwordEncoder.encode("password"))
                .role(role_4)
                .build();

        var user_2 = User.builder()
                .firstname("admin")
                .lastname("admin")
                .email("admin@gmail.com")
                .password(passwordEncoder.encode("password"))
                .role(role_2)
                .build();

        var user_3 = User.builder()
                .firstname("moderator")
                .lastname("moderator")
                .email("moderator@gmail.com")
                .password(passwordEncoder.encode("password"))
                .role(role_3)
                .build();

        var user_4 = User.builder()
                .firstname("user")
                .lastname("user")
                .email("user@gmail.com")
                .password(passwordEncoder.encode("password"))
                .role(role_1)
                .build();

        logger.info(role_1.getName());
        userRepository.saveAll(List.of(user_1, user_2, user_3, user_4));
    }
}