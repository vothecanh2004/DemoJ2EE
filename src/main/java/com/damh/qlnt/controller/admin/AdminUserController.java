package com.damh.qlnt.controller.admin;

import com.damh.qlnt.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminService adminService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", adminService.getAllUsers());
        return "admin/users/list";
    }

    @PostMapping("/toggle/{id}")
    public String toggleStatus(@PathVariable Long id) {
        adminService.toggleUserStatus(id);
        return "redirect:/admin/users";
    }
}
