package com.globe.gastronomy.backend.security;

import com.globe.gastronomy.backend.model.User;
import com.globe.gastronomy.backend.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));

        List<GrantedAuthority> roles = new ArrayList<>();
        user.getRoles().forEach(e -> roles.add(new SimpleGrantedAuthority(e.getRoleName())));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), roles);
    }
}
