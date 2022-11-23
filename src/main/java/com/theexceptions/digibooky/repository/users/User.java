package com.theexceptions.digibooky.repository.users;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private final String id;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final Role role;

    public User(String email, String password, String firstName, String lastName, Role role) {
        id = UUID.randomUUID().toString();
        this.email = validateEmail(email);
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    private String validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            //TODO: throw error
        }
        return email;
    }
}
