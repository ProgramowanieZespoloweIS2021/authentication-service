package com.eszop.authenticationservice.service;


import com.eszop.authenticationservice.domain.User;

public interface UserService {
    User encryptUserPassword(User user);

}
