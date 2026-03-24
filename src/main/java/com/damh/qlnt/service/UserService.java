package com.damh.qlnt.service;

import com.damh.qlnt.dto.UserRegistrationDto;
import com.damh.qlnt.entity.User;

public interface UserService {
    User registerNewUser(UserRegistrationDto registrationDto);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
