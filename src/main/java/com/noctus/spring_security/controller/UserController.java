package com.noctus.spring_security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    /**
     * test mapping
     * this mapping woks only for authenticated users
     * */

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Secured test endpoint only for users.");
    }
}
