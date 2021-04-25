package com.eszop.authenticationservice.service;

import com.eszop.authenticationservice.domain.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final BCryptPasswordEncoder encoder;

    @Value("${userServiceUrl}")
    private String userServiceUrl;

    public AuthService(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public void registerUser(AppUser user) {
        AppUser userToRegister = encryptUserPassword(user);
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<AppUser> request = new HttpEntity<>(userToRegister);
        restTemplate.postForObject(userServiceUrl + "/users", request, AppUser.class);
    }

    private AppUser encryptUserPassword(AppUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return user;
    }
}
