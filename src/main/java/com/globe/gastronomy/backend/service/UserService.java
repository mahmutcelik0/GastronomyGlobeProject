package com.globe.gastronomy.backend.service;

import com.globe.gastronomy.backend.dto.request.ChangePasswordRequest;
import com.globe.gastronomy.backend.dto.UserDto;
import com.globe.gastronomy.backend.dto.UserDtoPopulator;
import com.globe.gastronomy.backend.dto.response.ChangeRequestResponse;
import com.globe.gastronomy.backend.exception.RoleNotFoundException;
import com.globe.gastronomy.backend.exception.UserNotFoundException;
import com.globe.gastronomy.backend.model.Role;
import com.globe.gastronomy.backend.model.User;
import com.globe.gastronomy.backend.repository.UserRepository;
import com.globe.gastronomy.backend.utils.ImageUtil;
import com.globe.gastronomy.backend.utils.LogUtil;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public Set<UserDto> getAllUsers() throws UserNotFoundException {
        Set<UserDto> users = userRepository.findAll().stream().map(e -> new UserDtoPopulator().populate(e)).collect(Collectors.toSet());

        if (users.isEmpty()) {
            LogUtil.printLog("USER-NOT-FOUND", UserService.class);
            throw new UserNotFoundException("USER NOT FOUND");
        }

        return users;
    }

    public List<User> getUsers() throws UserNotFoundException {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            LogUtil.printLog("USER-NOT-FOUND", UserService.class);
            throw new UserNotFoundException("USER NOT FOUND");
        }

        return users;
    }

    public List<UserDto> getUsersByNameAndSurname(String firstName, String lastName) throws UserNotFoundException {
        List<UserDto> users = userRepository.findUserByFirstNameAndLastName(firstName, lastName).stream().map(e -> new UserDtoPopulator().populate(e)).toList();

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

        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));

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

        return new ResponseEntity<>("USER UPDATED SUCCESSFULLY", HttpStatus.OK);
    }

    public ResponseEntity deleteUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) return new ResponseEntity<>("USER IS NOT EXIST", HttpStatus.NOT_ACCEPTABLE);

        userRepository.delete(user.get());

        return new ResponseEntity<>("USER REMOVED SUCCESSFULLY", HttpStatus.OK);
    }

    public User getUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));
    }

    @Transactional
    public ResponseEntity<?> uploadImage(MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(authentication.getPrincipal().toString()).orElseThrow(() -> new UsernameNotFoundException("UsernameNotFound In User Service"));
        try {
            user.setProfileImage(ImageUtil.compressImage(file.getBytes()));
            userRepository.save(user);
        } catch (IOException e) {
            LogUtil.printLog("UPLOAD IMAGE PROBLEM", UserService.class);
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>("IMAGE UPLOADED SUCCESSFULLY!", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> getImage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(authentication.getPrincipal().toString()).orElseThrow(() -> new UsernameNotFoundException("UsernameNotFound In User Service"));
        if (user.getProfileImage().length == 0)
            return new ResponseEntity<>("User Does Not Have Any Profile Image", HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(ImageUtil.decompressImage(user.getProfileImage()));

    }

    public ResponseEntity<ChangeRequestResponse> changePassword(ChangePasswordRequest request) {
        if (request.getNewPassword().equals(request.getOldPassword()))
            return new ResponseEntity<>(new ChangeRequestResponse("New Password can't be same as old password"), HttpStatus.INTERNAL_SERVER_ERROR);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(authentication.getPrincipal().toString()).orElseThrow(() -> new UsernameNotFoundException("UsernameNotFound In User Service"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
            return new ResponseEntity<>(new ChangeRequestResponse("Password isn't correct"), HttpStatus.INTERNAL_SERVER_ERROR);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return new ResponseEntity<>(new ChangeRequestResponse("Password Changed Successfully!"), HttpStatus.OK);
    }
}


//AUTHENTICATION MUST CHANGE - PRINCIPAL -> EMAIL to USER