package com.theexceptions.digibooky.service;


import com.theexceptions.digibooky.exceptions.UnauthorizatedException;
import com.theexceptions.digibooky.exceptions.UserNotFoundException;
import com.theexceptions.digibooky.exceptions.WrongPasswordException;
import com.theexceptions.digibooky.repository.users.Role;
import com.theexceptions.digibooky.repository.users.User;
import com.theexceptions.digibooky.repository.users.UserRepository;
import com.theexceptions.digibooky.service.users.EmailPassword;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class SecurityService {

    private final UserRepository userRepository;

    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateAuthorization(String authorization, Role securityRole) {
        EmailPassword usernamePassword = getUsernamePassword(authorization);
        User user = userRepository.getUser(usernamePassword.email()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.doesPasswordMatch(usernamePassword.password())) {
            throw new WrongPasswordException();
        }
        if (!user.getRole().equals(securityRole)) {
            throw new UnauthorizatedException("You are not authorized to access this information");
        }

    }

    private EmailPassword getUsernamePassword(String authorization) {
        String decodedUsernameAndPassword = new String(Base64.getDecoder().decode(authorization.substring("Basic ".length())));
        String username = decodedUsernameAndPassword.substring(0, decodedUsernameAndPassword.indexOf(":"));
        String password = decodedUsernameAndPassword.substring(decodedUsernameAndPassword.indexOf(":") + 1);
        return new EmailPassword(username, password);
    }
}
