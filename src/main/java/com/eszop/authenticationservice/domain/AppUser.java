package com.eszop.authenticationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AppUser {
    private Integer id;
    private String username;
    private String password;
    private String role;
}
