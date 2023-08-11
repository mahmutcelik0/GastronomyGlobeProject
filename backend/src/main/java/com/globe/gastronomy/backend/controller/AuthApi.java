package com.globe.gastronomy.backend.controller;

import com.globe.gastronomy.backend.dto.BearerToken;
import com.globe.gastronomy.backend.dto.LoginDto;
import com.globe.gastronomy.backend.dto.UserDto;
import com.globe.gastronomy.backend.model.User;
import com.globe.gastronomy.backend.security.UsernamePasswordAuthenticationProvider;
import com.globe.gastronomy.backend.service.UserService;
import com.globe.gastronomy.backend.utils.JwtTokenUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthApi {
    private final UsernamePasswordAuthenticationProvider providerManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;


    public AuthApi(UsernamePasswordAuthenticationProvider providerManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.providerManager = providerManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        Authentication authentication = providerManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtTokenUtil.generateToken(user.getEmail(),user.getRoles());

        BearerToken bearerToken = new BearerToken(accessToken,"Bearer ");

        return ResponseEntity.ok(bearerToken);
    }

    @PostMapping("/register")
    public ResponseEntity addNewUser(@RequestBody UserDto userDto){
        return userService.addNewUser(userDto);
    }
}
