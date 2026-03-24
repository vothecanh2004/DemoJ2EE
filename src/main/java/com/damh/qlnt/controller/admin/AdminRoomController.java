package com.damh.qlnt.controller.admin;

import com.damh.qlnt.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/rooms")
@RequiredArgsConstructor
public class AdminRoomController {

    private final RoomService roomService;

    @GetMapping
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "admin/rooms/list";
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id) {
        roomService.approveRoom(id);
        return "redirect:/admin/rooms";
    }

    @PostMapping("/reject/{id}")
    public String reject(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestParam String reason) {
        roomService.rejectRoom(id, reason);
        return "redirect:/admin/rooms";
    }

    @PostMapping("/hide/{id}")
    public String hide(@PathVariable Long id) {
        roomService.hideRoom(id);
        return "redirect:/admin/rooms";
    }
}
