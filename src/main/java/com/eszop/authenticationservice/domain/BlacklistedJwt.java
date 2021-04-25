package com.eszop.authenticationservice.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class BlacklistedJwt {

    @Id
    private String token;

    private Date expirationDate;
}
