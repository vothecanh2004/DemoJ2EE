package com.damh.qlnt.controller.admin;

import com.damh.qlnt.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminService adminService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", adminService.getTotalUsers());
        model.addAttribute("totalOwners", adminService.getTotalOwners());
        model.addAttribute("totalRooms", adminService.getTotalRooms());
        model.addAttribute("totalContracts", adminService.getTotalActiveContracts());
        model.addAttribute("totalRevenue", adminService.getTotalRevenue());
        model.addAttribute("occupancyRate", adminService.getOccupancyRate());
        
        // New management stats
        model.addAttribute("pendingRooms", adminService.getPendingRoomsCount());
        model.addAttribute("pendingPosts", adminService.getPendingPostsCount());
        
        return "admin/dashboard";
    }
}
