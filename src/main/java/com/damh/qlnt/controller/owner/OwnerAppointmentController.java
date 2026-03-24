package com.damh.qlnt.controller.owner;

import com.damh.qlnt.entity.User;
import com.damh.qlnt.repository.UserRepository;
import com.damh.qlnt.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/owner/appointments")
@RequiredArgsConstructor
public class OwnerAppointmentController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;

    @GetMapping
    public String listAppointments(Model model, Principal principal) {
        User owner = userRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("appointments", appointmentService.getAppointmentsByOwner(owner));
        return "owner/appointments/list";
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable("id") Long id) {
        appointmentService.approveAppointment(id);
        return "redirect:/owner/appointments";
    }

    @PostMapping("/reject/{id}")
    public String reject(@PathVariable("id") Long id, @RequestParam("reason") String reason) {
        appointmentService.rejectAppointment(id, reason);
        return "redirect:/owner/appointments";
    }

    @PostMapping("/complete/{id}")
    public String complete(@PathVariable("id") Long id) {
        appointmentService.completeAppointment(id);
        return "redirect:/owner/appointments";
    }
}
