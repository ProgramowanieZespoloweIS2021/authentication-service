package com.eszop.authenticationservice.controller;

import com.eszop.authenticationservice.domain.User;
import com.eszop.authenticationservice.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
//????????????????
@RequestMapping(value = "/auth")
public class AuthController {

    private final UserService userService;

    @Value("${userServiceUrl}")
    private String userServiceUrl;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // get loged username/user
    @GetMapping("/logged-user")
    @ResponseStatus(HttpStatus.CREATED)
    public void getLoggedUser(@RequestBody User user){

    }

    // login
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public void login(@RequestBody User user){
        //return token if user authenticated, can be retrived from user-service for auth
    }

    // rename url to match rest restrictions
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody User user){
        user = userService.encryptUserPassword(user);
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<User> request = new HttpEntity<>(user);
        restTemplate.postForObject(userServiceUrl + "/users", request, User.class);
    }

    // logout
}
