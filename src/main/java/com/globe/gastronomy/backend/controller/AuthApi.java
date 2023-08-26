package com.globe.gastronomy.backend.controller;

import com.globe.gastronomy.backend.command.PasswordEmailResponse;
import com.globe.gastronomy.backend.dto.*;
import com.globe.gastronomy.backend.facade.EmailFacade;
import com.globe.gastronomy.backend.model.User;
import com.globe.gastronomy.backend.security.UsernamePasswordAuthenticationProvider;
import com.globe.gastronomy.backend.service.UserService;
import com.globe.gastronomy.backend.utils.JwtTokenUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthApi {
    private final UsernamePasswordAuthenticationProvider providerManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final EmailFacade emailFacade;

    public AuthApi(UsernamePasswordAuthenticationProvider providerManager, JwtTokenUtil jwtTokenUtil, UserService userService, EmailFacade emailFacade) {
        this.providerManager = providerManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.emailFacade = emailFacade;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        Authentication authentication = providerManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtTokenUtil.generateToken(new UserDtoPopulator().populate(user), user.getRoles());

        BearerToken bearerToken = new BearerToken(accessToken, "Bearer ");

        return ResponseEntity.ok(bearerToken);
    }

    @PostMapping("/register")
    public ResponseEntity addNewUser(@RequestBody UserDto userDto) {
        return userService.addNewUser(userDto);
    }

    @GetMapping("/hello")
    public String getHello() {
        return "HELLOO";
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<PasswordEmailResponse> forgotPasswordEmail(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        return ResponseEntity.ok(emailFacade.sendForgotPasswordEmail(forgotPasswordDto.getLanguage(), forgotPasswordDto.getEmail()));
    }
}
