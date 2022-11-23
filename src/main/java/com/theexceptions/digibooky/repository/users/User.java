package com.theexceptions.digibooky.repository.users;

import java.util.UUID;

public class User {
    private final String id;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final Role role;

    public User(String email, String password, String firstName, String lastName, Role role) {
        id = UUID.randomUUID().toString();
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}
