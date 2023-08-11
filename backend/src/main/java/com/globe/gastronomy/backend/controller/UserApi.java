package com.globe.gastronomy.backend.controller;

import com.globe.gastronomy.backend.dto.request.ChangePasswordRequest;
import com.globe.gastronomy.backend.dto.UserDto;
import com.globe.gastronomy.backend.dto.response.ChangeRequestResponse;
import com.globe.gastronomy.backend.exception.UserNotFoundException;
import com.globe.gastronomy.backend.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PatchMapping("/addNewRole")
    public ResponseEntity addNewRoleToUser(@RequestBody @Valid UserDto userDto, @RequestParam String roleName){
        return userService.addNewRoleToUser(userDto,roleName);
    }

    @PatchMapping("/updateNameOfUser")
    public ResponseEntity updateNameOfUser(@RequestBody @Valid UserDto userDto, @RequestParam String firstName, @RequestParam String lastName){
        return userService.updateNameOfUser(userDto,firstName,lastName);
    }

    @DeleteMapping
    public ResponseEntity deleteUserByEmail(@RequestParam @Email String email){
        return userService.deleteUser(email);
    }


    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile image){
        return userService.uploadImage(image);
    }

    @GetMapping("/getImage")
    public ResponseEntity<?> getImage(){
        return userService.getImage();
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<ChangeRequestResponse> changePassword(@RequestBody @Valid ChangePasswordRequest request){
        return userService.changePassword(request);
    }

}