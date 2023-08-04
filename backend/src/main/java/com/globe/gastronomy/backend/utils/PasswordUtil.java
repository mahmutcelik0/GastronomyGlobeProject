package com.globe.gastronomy.backend.utils;

import com.globe.gastronomy.backend.dto.UserDto;
import com.globe.gastronomy.backend.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    private final PasswordEncoder passwordEncoder;

    public PasswordUtil(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void prepareUserPassword(User user, UserDto userDto){
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    }
}
