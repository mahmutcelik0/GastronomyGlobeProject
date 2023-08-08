package com.globe.gastronomy.backend.service;

import com.globe.gastronomy.backend.dto.UserDto;
import com.globe.gastronomy.backend.dto.UserDtoPopulator;
import com.globe.gastronomy.backend.exception.RoleNotFoundException;
import com.globe.gastronomy.backend.exception.UserNotFoundException;
import com.globe.gastronomy.backend.model.Role;
import com.globe.gastronomy.backend.model.User;
import com.globe.gastronomy.backend.repository.UserRepository;
import com.globe.gastronomy.backend.utils.LogUtil;
import com.globe.gastronomy.backend.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordUtil passwordUtil;

    private final RoleService roleService;

    @Autowired
    private PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordUtil passwordUtil, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
        this.roleService = roleService;
    }

    public Set<UserDto> getAllUsers() throws UserNotFoundException {
        Set<UserDto> users = userRepository.findAll()
                .stream()
                .map(e -> new UserDtoPopulator().populate(e))
                .collect(Collectors.toSet());

        if (users.isEmpty()) {
            LogUtil.printLog("USER-NOT-FOUND", UserService.class);
            throw new UserNotFoundException("USER NOT FOUND");
        }
        return users;
    }

    public List<UserDto> getUsersByNameAndSurname(String firstName, String lastName) throws UserNotFoundException {
        List<UserDto> users = userRepository.findUserByFirstNameAndLastName(firstName, lastName)
                .stream()
                .map(e -> new UserDtoPopulator().populate(e))
                .toList();

        if (users.isEmpty()) {
            LogUtil.printLog("USER-NOT-FOUND", UserService.class);
            throw new UserNotFoundException("USER NOT FOUND");
        }

        return users;
    }

    public ResponseEntity addNewUser(UserDto userDto) {
        Optional<User> user = userRepository.findUserByFirstNameAndLastNameAndEmail(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail());

        if (user.isPresent()) return new ResponseEntity<>("User Already Exists!", HttpStatus.CONFLICT);

        User newUser = new UserDtoPopulator().reverseConverter(userDto);

        passwordUtil.prepareUserPassword(newUser, userDto);

        newUser.setPassword(encoder.encode(userDto.getPassword()));

        try {
            roleService.setUserRoles(newUser, userDto);
        } catch (RoleNotFoundException e) {
            userRepository.save(newUser);
            return new ResponseEntity<>("User Saved without any role", HttpStatus.OK);
        }

        userRepository.save(newUser);

        return new ResponseEntity<>("User Saved Successfully", HttpStatus.OK);
    }

    public ResponseEntity addNewRoleToUser(UserDto userDto, String roleName) {
        Optional<User> user = userRepository.findUserByFirstNameAndLastNameAndEmail(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail());

        if (user.isEmpty()) return new ResponseEntity<>("USER IS NOT EXIST", HttpStatus.NOT_ACCEPTABLE);

        Role role = roleService.findRoleByRoleName(roleName);

        user.get().getRoles().add(role);

        userRepository.save(user.get());

        return new ResponseEntity<>("NEW ROLE ADDED TO USER SUCCESSFULLY!", HttpStatus.OK);
    }

    public ResponseEntity updateNameOfUser(UserDto userDto, String newFirstName, String newLastName) {
        Optional<User> user = userRepository.findUserByFirstNameAndLastNameAndEmail(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail());

        if (user.isEmpty()) return new ResponseEntity<>("USER IS NOT EXIST", HttpStatus.NOT_ACCEPTABLE);

        user.get().setFirstName(newFirstName);
        user.get().setLastName(newLastName);

        userRepository.save(user.get());

        return new ResponseEntity<>("USER UPDATED SUCCESSFULLY",HttpStatus.OK);
    }

    public ResponseEntity deleteUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) return new ResponseEntity<>("USER IS NOT EXIST", HttpStatus.NOT_ACCEPTABLE);

        userRepository.delete(user.get());

        return new ResponseEntity<>("USER REMOVED SUCCESSFULLY",HttpStatus.OK);
    }
}
