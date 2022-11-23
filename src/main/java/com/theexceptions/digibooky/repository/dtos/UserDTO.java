package com.theexceptions.digibooky.repository.dtos;

import com.theexceptions.digibooky.repository.users.Role;

public class UserDTO {
    private final String id;
    private final String email;
    private final Role role;

    public UserDTO(String id, String email, Role role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
