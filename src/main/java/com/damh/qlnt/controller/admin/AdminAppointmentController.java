package com.damh.qlnt.controller.admin;

import com.damh.qlnt.entity.Appointment;
import com.damh.qlnt.entity.AppointmentStatus;
import com.damh.qlnt.entity.User;
import com.damh.qlnt.service.AppointmentService;
import com.damh.qlnt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/appointments")
@RequiredArgsConstructor
public class AdminAppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;

    @GetMapping
    public String listAppointments(Model model) {
        List<Appointment> allAppointments = appointmentService.getAllAppointments();
        model.addAttribute("appointments", allAppointments);
        
        // Clearer grouping logic for Admin stats
        Map<User, List<Appointment>> ownerStats = new java.util.HashMap<>();
        for (Appointment a : allAppointments) {
            ownerStats.computeIfAbsent(a.getOwner(), k -> new java.util.ArrayList<>()).add(a);
        }
        model.addAttribute("ownerStats", ownerStats);
        
        return "admin/appointments/list";
    }

    @PostMapping("/cancel/{id}")
    public String cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelByAdmin(id);
        return "redirect:/admin/appointments";
    }

    @PostMapping("/owners/{ownerId}/penalize")
    public String penalizeOwner(@PathVariable Long ownerId, @RequestParam int penalty) {
        userService.updateReputation(ownerId, -penalty);
        return "redirect:/admin/appointments";
    }
}
