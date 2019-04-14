package com.ttn.linksharing.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String fileName;
    private Boolean isActive=true;
    private Boolean isAdmin=false;

    @Transient
    private String confirmPassword;

    public User(String firstName, String lastName, String email, String username, String password, String fileName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.fileName = fileName;
    }

    public User(String firstName, String lastName, String email, String username, String password, String fileName, Boolean isAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.fileName = fileName;
        this.isAdmin = isAdmin;
    }

    public User() {
    }
}
