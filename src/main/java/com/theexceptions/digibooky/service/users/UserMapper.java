package com.theexceptions.digibooky.service.users;

import com.theexceptions.digibooky.repository.dtos.UserDTO;
import com.theexceptions.digibooky.repository.users.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getRole());
    }
}
