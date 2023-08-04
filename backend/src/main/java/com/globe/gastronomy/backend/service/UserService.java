package com.globe.gastronomy.backend.service;

import com.globe.gastronomy.backend.dto.UserDto;
import com.globe.gastronomy.backend.dto.UserDtoPopulator;
import com.globe.gastronomy.backend.exception.UserNotFoundException;
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

    @Autowired
    private PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordUtil passwordUtil) {
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
    }

    public Set<UserDto> getAllUsers() throws UserNotFoundException {
        Set<UserDto> users = userRepository.findAll()
                .stream()
                .map(e -> new UserDtoPopulator().populate(e))
                .collect(Collectors.toSet());

        if (users.isEmpty()){
            LogUtil.printLog("USER-NOT-FOUND",UserService.class);
            throw new UserNotFoundException("USER NOT FOUND");
        }
        return users;
    }

    public List<UserDto> getUsersByNameAndSurname(String firstName, String lastName) throws UserNotFoundException {
        List<UserDto> users = userRepository.findUserByFirstNameAndLastName(firstName,lastName)
                .stream()
                .map(e -> new UserDtoPopulator().populate(e))
                .toList();

        if (users.isEmpty()){
            LogUtil.printLog("USER-NOT-FOUND",UserService.class);
            throw new UserNotFoundException("USER NOT FOUND");
        }

        return users;
    }

    public ResponseEntity addNewUser(UserDto userDto) {
        Optional<User> user = userRepository.findUserByFirstNameAndLastNameAndEmail(userDto.getFirstName(),userDto.getLastName(),userDto.getEmail());

        if(user.isPresent()) return new ResponseEntity<>("User Already Exists!", HttpStatus.CONFLICT);

        User newUser = new UserDtoPopulator().reverseConverter(userDto);

        passwordUtil.prepareUserPassword(newUser,userDto);

        newUser.setPassword(encoder.encode(userDto.getPassword()));
        userRepository.save(newUser);

        return new ResponseEntity<>("User Saved Successfully",HttpStatus.OK);
    }
}
