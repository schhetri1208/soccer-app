package com.soccerapp.service;

import com.soccerapp.model.User;
import com.soccerapp.repository.UserRepository;
import com.soccerapp.service.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
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
}
