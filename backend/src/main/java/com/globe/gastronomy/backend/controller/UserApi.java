package com.globe.gastronomy.backend.controller;

import com.globe.gastronomy.backend.dto.UserDto;
import com.globe.gastronomy.backend.exception.UserNotFoundException;
import com.globe.gastronomy.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
public class UserApi {

    private final UserService userService;

    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Set<UserDto> getAllUsers() throws UserNotFoundException {
        return userService.getAllUsers();
    }

    @GetMapping("/findByNameAndSurname")
    public List<UserDto> getUsersByNameAndSurname(@RequestParam String firstName,@RequestParam String lastName) throws UserNotFoundException {
        return userService.getUsersByNameAndSurname(firstName,lastName);
    }

    @PostMapping
    public ResponseEntity addNewUser(@RequestBody @Valid UserDto userDto){
        return userService.addNewUser(userDto);
    }


}