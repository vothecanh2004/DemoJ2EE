package com.damh.qlnt.controller.user;

import com.damh.qlnt.entity.Appointment;
import com.damh.qlnt.entity.Room;
import com.damh.qlnt.entity.User;
import com.damh.qlnt.repository.UserRepository;
import com.damh.qlnt.service.AppointmentService;
import com.damh.qlnt.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/user/appointments")
@RequiredArgsConstructor
public class UserAppointmentController {

    private final AppointmentService appointmentService;
    private final RoomService roomService;
    private final UserRepository userRepository;

    @GetMapping
    public String listAppointments(Model model, Principal principal) {
        User tenant = userRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("appointments", appointmentService.getAppointmentsByTenant(tenant));
        return "user/appointments/list";
    }

    @PostMapping("/book/{roomId}")
    public String bookAppointment(@PathVariable Long roomId, 
                                  @RequestParam("appointmentDate") String appointmentDateStr, 
                                  Principal principal) {
        User tenant = userRepository.findByUsername(principal.getName()).orElseThrow();
        Room room = roomService.getRoomById(roomId).orElseThrow();
        
        Appointment apt = Appointment.builder()
                .tenant(tenant)
                .owner(room.getOwner())
                .room(room)
                .appointmentDate(LocalDateTime.parse(appointmentDateStr))
                .build();
                
        appointmentService.bookAppointment(apt);
        return "redirect:/user/appointments";
    }
}
