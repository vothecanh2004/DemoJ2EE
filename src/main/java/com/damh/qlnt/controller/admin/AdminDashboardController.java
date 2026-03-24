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
        model.addAttribute("totalRooms", adminService.getTotalRooms());
        model.addAttribute("totalContracts", adminService.getTotalActiveContracts());
        // For revenue we could mock it or aggregate from contracts later
        model.addAttribute("totalRevenue", adminService.getTotalActiveContracts() * 1500000); 
        return "admin/dashboard";
    }
}
