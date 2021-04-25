package com.eszop.authenticationservice.controller;

import com.eszop.authenticationservice.domain.AppUser;
import com.eszop.authenticationservice.service.AuthService;
import com.eszop.authenticationservice.service.CustomUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CustomUserDetailsService userService;

    private final AuthService authService;

    public AuthController(CustomUserDetailsService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        return Optional.ofNullable(principal)
                .map(Principal::getName)
                .map(userService::loadUserByUsername)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody AppUser user){
        authService.registerUser(user);
    }
}
