package com.theexceptions.digibooky.repository.dtos;

import com.theexceptions.digibooky.repository.users.Role;

public record CreateModeratorDTO(String email, String password, String firstName, String lastName, Role role) {
}
