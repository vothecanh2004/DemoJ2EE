package com.damh.qlnt.service.impl;

import com.damh.qlnt.entity.ContractStatus;
import com.damh.qlnt.entity.User;
import com.damh.qlnt.repository.ContractRepository;
import com.damh.qlnt.repository.RoomRepository;
import com.damh.qlnt.repository.UserRepository;
import com.damh.qlnt.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ContractRepository contractRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public long getTotalRooms() {
        return roomRepository.count();
    }

    @Override
    public long getTotalActiveContracts() {
        return contractRepository.findAll().stream()
                .filter(c -> c.getStatus() == ContractStatus.ACTIVE)
                .count();
    }
}
