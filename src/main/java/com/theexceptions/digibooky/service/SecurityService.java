package com.theexceptions.digibooky.service;


import com.theexceptions.digibooky.exceptions.UserNotFoundException;
import com.theexceptions.digibooky.repository.users.User;
import com.theexceptions.digibooky.repository.users.UserRepository;
import com.theexceptions.digibooky.service.users.EmailPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class SecurityService {
    private final Logger logger = LoggerFactory.getLogger(SecurityService.class);

    private UserRepository userRepository;

    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateAuthorization(String authorization) {
//        EmailPassword usernamePassword = getUsernamePassword(authorization);
//        User user = userRepository.getUser(usernamePassword.getEmail()).orElseThrow(new UserNotFoundException("User not found")));
//        if(user == null) {
//            logger.error("Unknown user" + usernamePassword.getUsername());
//            throw new UnknownUserException();
//        }
//        if(!user.doesPasswordMatch(usernamePassword.getPassword())) {
//            logger.error("Password does not match for user " + usernamePassword.getUsername());
//            throw new WrongPasswordException();
//        }
//        if(!user.canHaveAccessTo(feature)) {
//            logger.error("User " + usernamePassword.getUsername() + " does not have access to " + feature);
//            throw new UnauthorizatedException();
//        }

    }

    private EmailPassword getUsernamePassword(String authorization) {
        String decodedUsernameAndPassword = new String(Base64.getDecoder().decode(authorization.substring("Basic ".length())));
        String username = decodedUsernameAndPassword.substring(0, decodedUsernameAndPassword.indexOf(":"));
        String password = decodedUsernameAndPassword.substring(decodedUsernameAndPassword.indexOf(":") + 1);
        return new EmailPassword(username, password);
    }
}
