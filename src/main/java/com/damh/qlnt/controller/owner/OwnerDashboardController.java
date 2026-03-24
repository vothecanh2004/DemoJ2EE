package com.damh.qlnt.controller.owner;

import com.damh.qlnt.entity.User;
import com.damh.qlnt.repository.UserRepository;
import com.damh.qlnt.service.AppointmentService;
import com.damh.qlnt.service.ContractService;
import com.damh.qlnt.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;

@Controller
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerDashboardController {

    private final RoomService roomService;
    private final AppointmentService appointmentService;
    private final ContractService contractService;
    private final UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        User owner = userRepository.findByUsername(principal.getName()).orElseThrow();
        
        model.addAttribute("username", owner.getUsername());
        model.addAttribute("totalRooms", roomService.getRoomsByOwner(owner).size());
        model.addAttribute("pendingAppointments", appointmentService.getAppointmentsByOwner(owner)
                .stream().filter(a -> a.getStatus().name().equals("PENDING")).count());
        model.addAttribute("activeContracts", contractService.getContractsByOwner(owner)
                .stream().filter(c -> c.getStatus().name().equals("ACTIVE")).count());
        
        return "owner/dashboard";
    }}
