package com.globe.gastronomy.backend.security;

import com.globe.gastronomy.backend.model.User;
import com.globe.gastronomy.backend.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UsernamePasswordAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = userService.getUserByEmail(authentication.getName()); // Name == email

        boolean matches = passwordEncoder.matches(authentication.getCredentials().toString(),user.getPassword()); //Credentials == password

        if(!matches) throw new BadCredentialsException("WRONG LOGIN INFORMATION PROVIDED");

        List<GrantedAuthority> roles = new ArrayList<>();

        user.getRoles().forEach(e -> roles.add(new SimpleGrantedAuthority(e.getRoleName())));

        return new UsernamePasswordAuthenticationToken(user,user.getPassword(),roles);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)); //copy from AbstractUserDetailsAuthProvider
    }
}
