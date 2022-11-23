package com.theexceptions.digibooky.API;

import com.theexceptions.digibooky.repository.dtos.CreateMemberDTO;
import com.theexceptions.digibooky.repository.dtos.UserDTO;
import com.theexceptions.digibooky.service.users.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO createUser(@RequestBody CreateMemberDTO createMemberDTO) {
      return   userService.createNewMember(createMemberDTO);
    }
}
