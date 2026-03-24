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
    private final com.damh.qlnt.repository.PostRepository postRepository;

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
        return userRepository.findAll().stream().filter(u -> u.getRole() == com.damh.qlnt.entity.Role.USER).count();
    }
    
    @Override
    public long getTotalOwners() {
        return userRepository.findAll().stream().filter(u -> u.getRole() == com.damh.qlnt.entity.Role.OWNER).count();
    }

    @Override
    public long getTotalRooms() {
        return roomRepository.count();
    }
    
    @Override
    public double getOccupancyRate() {
        long totalRooms = roomRepository.count();
        if (totalRooms == 0) return 0.0;
        long rentedRooms = roomRepository.findAll().stream().filter(r -> r.getStatus() == com.damh.qlnt.entity.RoomStatus.RENTED).count();
        return (double) rentedRooms / totalRooms * 100.0;
    }

    @Override
    public double getTotalRevenue() {
        return roomRepository.findAll().stream()
                .filter(r -> r.getStatus() == com.damh.qlnt.entity.RoomStatus.RENTED)
                .mapToDouble(r -> r.getPrice().doubleValue())
                .sum();
    }

    @Override
    public long getTotalActiveContracts() {
        return contractRepository.findAll().stream()
                .filter(c -> c.getStatus() == ContractStatus.ACTIVE)
                .count();
    }

    @Override
    public long getPendingRoomsCount() {
        return roomRepository.findAll().stream()
                .filter(r -> r.getApprovalStatus() == com.damh.qlnt.entity.ApprovalStatus.PENDING)
                .count();
    }

    @Override
    public long getPendingPostsCount() {
        return postRepository.findAll().stream()
                .filter(p -> p.getStatus() == com.damh.qlnt.entity.PostStatus.PENDING)
                .count();
    }
}
