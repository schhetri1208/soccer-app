package com.soccerapp.controller;

import com.soccerapp.security.JwtUtil;
import com.soccerapp.service.UserService;
import com.soccerapp.service.dto.RegisterRequest;
import com.soccerapp.service.dto.UserProfileRequest;
import com.soccerapp.service.dto.UserProfileResponse;
import com.soccerapp.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.soccerapp.model.User;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfiles(HttpServletRequest httpServletRequest) {
        String email = RequestUtil.getEmail(httpServletRequest);
        return ResponseEntity.ok(userService.getProfile(email));
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(HttpServletRequest httpServletRequest,
                                                             @RequestBody UserProfileRequest request) {
        String email = RequestUtil.getEmail(httpServletRequest);
        userService.updateProfile(email, request);
        return ResponseEntity.ok("Profile updated successfully.");
    }
}
