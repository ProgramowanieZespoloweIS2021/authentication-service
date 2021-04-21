package com.eszop.authenticationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AppUser {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String surname;
}
