package com.eszop.authenticationservice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String firstName;

    private String surname;

    // should be array of chars, bcs string stays in string pool and someone can easliy extract password from heap
    // just for student project purposes
    private String password;


    private String email;

}
