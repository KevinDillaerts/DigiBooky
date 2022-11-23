package com.theexceptions.digibooky.service.users;

import com.theexceptions.digibooky.repository.dtos.UserDTO;
import com.theexceptions.digibooky.repository.users.User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class UserMapper {


    public UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getRole());
    }

    public List<UserDTO> getUserDTOList(Collection<User> users) {
        return users.stream()
                .map(this::toUserDTO)
                .toList();
    }
}
