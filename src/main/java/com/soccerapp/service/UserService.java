package com.soccerapp.service;

import com.soccerapp.model.User;
import com.soccerapp.repository.UserRepository;
import com.soccerapp.service.dto.RegisterRequest;
import com.soccerapp.service.dto.UserProfileRequest;
import com.soccerapp.service.dto.UserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public User register(RegisterRequest request) {

        User existingUser = userRepository.findByEmail(request.email());
        if (existingUser != null){
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(new BCryptPasswordEncoder().encode(request.password()));
        user.setLocation(request.location());

        return userRepository.save(user);
    }

    public UserProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email);
        return new UserProfileResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getLocation()
        );
    }

    public void updateProfile(String email, UserProfileRequest request) {
        User user = userRepository.findByEmail(email);

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setLocation(request.location());
        if (request.newPassword()!= null && !request.newPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.newPassword()));
        }
        userRepository.save(user);
    }
}
