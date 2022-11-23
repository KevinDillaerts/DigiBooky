package com.theexceptions.digibooky.repository.users;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {
    private final Map<String, User> users;

    public UserRepository() {
        users = new HashMap<>();
    }

    public Map<String, User> getUsers() {
        return users;
    }
}
