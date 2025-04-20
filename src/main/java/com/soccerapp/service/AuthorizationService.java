package com.soccerapp.service;

import com.soccerapp.model.User;
import com.soccerapp.repository.UserRepository;
import com.soccerapp.security.CustomUserDetailsService;
import com.soccerapp.security.JwtUtil;
import com.soccerapp.service.dto.AuthResponse;
import com.soccerapp.service.dto.LoginRequest;
import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthorizationService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthResponse authenticateUser(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (AuthenticationException e) {
            System.out.println("AUTH FAILED: " + e.getMessage());
            throw e;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        String token = jwtUtil.generateToken(userDetails.getUsername());

        User user = userRepository.findByEmail(request.email());
        return new AuthResponse(token, user.getEmail(), user.getFirstName() + " " + user.getLastName());
    }

}
