package com.eszop.authenticationservice.controller;

import com.eszop.authenticationservice.service.CustomUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CustomUserDetailsService service;

    public AuthController(CustomUserDetailsService service) {
        this.service = service;
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        return Optional.ofNullable(principal)
                .map(Principal::getName)
                .map(service::loadUserByUsername)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
