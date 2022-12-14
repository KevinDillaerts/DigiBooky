package com.theexceptions.digibooky.service.users;

import com.theexceptions.digibooky.exceptions.FieldIsEmptyException;
import com.theexceptions.digibooky.exceptions.MemberAlreadyExistsException;
import com.theexceptions.digibooky.exceptions.UnauthorizedException;
import com.theexceptions.digibooky.repository.dtos.CreateMemberDTO;
import com.theexceptions.digibooky.repository.dtos.CreateModeratorDTO;
import com.theexceptions.digibooky.repository.dtos.UserDTO;
import com.theexceptions.digibooky.repository.users.Member;
import com.theexceptions.digibooky.repository.users.Role;
import com.theexceptions.digibooky.repository.users.User;
import com.theexceptions.digibooky.repository.users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO createNewMember(CreateMemberDTO createMemberDTO) {
        if (createMemberDTO.getSSID() == null || createMemberDTO.getSSID().equals("")) {
            throw new FieldIsEmptyException("the SSID field cannot be empty");
        }
        if (createMemberDTO.getEmail() == null) {
            throw new FieldIsEmptyException("the email field cannot be empty");
        }
        if (userRepository.containsUser(createMemberDTO.getSSID(), createMemberDTO.getEmail())) {
            throw new MemberAlreadyExistsException("This user's email or ssid already exists");
        }
        Member userToAdd = new Member(createMemberDTO.getEmail(), createMemberDTO.getPassword(), createMemberDTO.getFirstName(), createMemberDTO.getLastName(), createMemberDTO.getSSID(), createMemberDTO.getAddress());
        userRepository.addUser(userToAdd);
        return userMapper.toUserDTO(userToAdd);
    }

    public UserDTO createNewModerator(CreateModeratorDTO createModeratorDTO) {
        if (userRepository.containsEmail(createModeratorDTO.email())) {
            throw new MemberAlreadyExistsException("This user's email or ssid already exists");
        }
        if (createModeratorDTO.role().equals(Role.MEMBER)) {
            throw new UnauthorizedException(("You cannot create a new member"));
        }
        User userToAdd = new User(createModeratorDTO.email(), createModeratorDTO.password(),
                createModeratorDTO.firstName(), createModeratorDTO.lastName(), createModeratorDTO.role());
        userRepository.addUser(userToAdd);
        return userMapper.toUserDTO(userToAdd);
    }

    public List<UserDTO> getAllUsers() {
        return userMapper.getUserDTOList(userRepository.getUsers().values());
    }
}
