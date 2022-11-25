package com.theexceptions.digibooky.API;

import com.theexceptions.digibooky.exceptions.UnauthorizedException;
import com.theexceptions.digibooky.repository.dtos.CreateMemberDTO;
import com.theexceptions.digibooky.repository.dtos.CreateModeratorDTO;
import com.theexceptions.digibooky.repository.dtos.UserDTO;
import com.theexceptions.digibooky.repository.users.Role;
import com.theexceptions.digibooky.service.SecurityService;
import com.theexceptions.digibooky.service.users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    private final SecurityService securityService;

    public UserController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody CreateMemberDTO createMemberDTO) {
        return userService.createNewMember(createMemberDTO);
    }

    @PostMapping(path = "/admin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createModerator(@RequestHeader String authorization, @RequestBody CreateModeratorDTO createModeratorDTO) {
        securityService.validateAuthorization(authorization, Role.ADMIN);
        return userService.createNewModerator(createModeratorDTO);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAllUsers(@RequestHeader String authorization) {
        securityService.validateAuthorization(authorization, Role.ADMIN);
        return userService.getAllUsers();
    }
}
