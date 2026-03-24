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
    public String approve(@PathVariable("id") Long id) {
        System.out.println(">>> Controller: Approving Room ID: " + id);
        roomService.approveRoom(id);
        return "redirect:/admin/rooms";
    }

    @PostMapping("/reject/{id}")
    public String reject(@PathVariable("id") Long id, @org.springframework.web.bind.annotation.RequestParam("reason") String reason) {
        roomService.rejectRoom(id, reason);
        return "redirect:/admin/rooms";
    }

    @PostMapping("/hide/{id}")
    public String hide(@PathVariable("id") Long id) {
        System.out.println(">>> Controller: Hiding Room ID: " + id);
        roomService.hideRoom(id);
        return "redirect:/admin/rooms";
    }

    @PostMapping("/ban/{id}")
    public String ban(@PathVariable("id") Long id) {
        System.out.println(">>> Controller: Banning Room ID: " + id);
        roomService.banRoom(id);
        return "redirect:/admin/rooms";
    }
}
