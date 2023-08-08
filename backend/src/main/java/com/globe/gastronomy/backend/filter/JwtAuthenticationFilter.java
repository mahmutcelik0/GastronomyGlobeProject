package com.globe.gastronomy.backend.filter;

import com.globe.gastronomy.backend.security.CustomUserDetailsService;
import com.globe.gastronomy.backend.utils.JwtTokenUtil;
import com.globe.gastronomy.backend.utils.LogUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtUtilities;
    private final CustomUserDetailsService customerUserDetailsService;

    public JwtAuthenticationFilter(JwtTokenUtil jwtUtilities, CustomUserDetailsService customerUserDetailsService) {
        this.jwtUtilities = jwtUtilities;
        this.customerUserDetailsService = customerUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtUtilities.getToken(request);

        if (token != null && jwtUtilities.validateToken(token)) {
            String email = jwtUtilities.extractUsername(token);

            UserDetails userDetails = customerUserDetailsService.loadUserByUsername(email);
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                LogUtil.printLog("authenticated user with email :"+email, JwtAuthenticationFilter.class);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }
        filterChain.doFilter(request, response);
    }
}
