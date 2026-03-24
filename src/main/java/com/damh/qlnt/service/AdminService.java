package com.damh.qlnt.service;

import com.damh.qlnt.entity.User;
import java.util.List;

public interface AdminService {
    List<User> getAllUsers();
    void toggleUserStatus(Long userId);
    
    // Statistics
    long getTotalUsers();
    long getTotalOwners();
    long getTotalRooms();
    long getTotalActiveContracts();
    double getOccupancyRate();
    double getTotalRevenue();
    
    long getPendingRoomsCount();
    long getPendingPostsCount();
}
