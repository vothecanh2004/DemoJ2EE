package com.damh.qlnt.service.impl;

import com.damh.qlnt.dto.UserRegistrationDto;
import com.damh.qlnt.entity.Role;
import com.damh.qlnt.entity.User;
import com.damh.qlnt.repository.UserRepository;
import com.damh.qlnt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerNewUser(UserRegistrationDto registrationDto) {
        if (existsByUsername(registrationDto.getUsername())) {
            throw new IllegalArgumentException("Username is already taken.");
        }
        if (existsByEmail(registrationDto.getEmail())) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        Role userRole;
        if ("OWNER".equalsIgnoreCase(registrationDto.getRole())) {
            userRole = Role.OWNER;
        } else if ("USER".equalsIgnoreCase(registrationDto.getRole())) {
            userRole = Role.USER;
        } else {
            throw new IllegalArgumentException("Invalid role selected. Must be USER or OWNER.");
        }

        User user = User.builder()
                .username(registrationDto.getUsername())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .email(registrationDto.getEmail())
                .fullName(registrationDto.getFullName())
                .phone(registrationDto.getPhone())
                .role(userRole)
                .enabled(true)
                .build();

        return userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public void updateReputation(Long userId, int delta) {
        User user = findById(userId);
        int newScore = user.getReputationScore() + delta;
        // Clamp between 0 and 100
        newScore = Math.max(0, Math.min(100, newScore));
        user.setReputationScore(newScore);
        userRepository.save(user);
    }
}
