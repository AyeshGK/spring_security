package com.noctus.spring_security.config;

import com.noctus.spring_security.config.jwt.JwtAuthenticationFilter;
import com.noctus.spring_security.model.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        create whitelist array
//        String[] whitelist = {"/api/v1/auth/**", "/api/v1/home**"};
//        add whitelist to permitAll()

//        http.
//                csrf().disable()
//                .authorizeHttpRequests()
//                .requestMatchers("/api/v1/auth/**", "/api/v1/home**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .authorizeHttpRequests()
//                .requestMatchers("/api/v1/user/test").hasAuthority("ADMIN")
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        http.
                csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/**", "/api/v1/home**").permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/admin/**").hasAuthority(ERole.ADMIN.name())
                .requestMatchers("/api/v1/user/**").hasAuthority(ERole.USER.name())
                .requestMatchers("/api/v1/multi/**").hasAnyAuthority(
                        ERole.MODERATOR.name(),
                        ERole.ADMIN.name(),
                        ERole.SUPER_ADMIN.name()
                )
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
