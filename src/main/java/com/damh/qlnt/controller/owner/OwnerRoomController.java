package com.damh.qlnt.controller.owner;

import com.damh.qlnt.entity.ApprovalStatus;
import com.damh.qlnt.entity.Room;
import com.damh.qlnt.entity.User;
import com.damh.qlnt.repository.UserRepository;
import com.damh.qlnt.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/owner/rooms")
@RequiredArgsConstructor
public class OwnerRoomController {

    private final RoomService roomService;
    private final UserRepository userRepository;

    @GetMapping
    public String listRooms(Model model, Principal principal) {
        User owner = userRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("rooms", roomService.getRoomsByOwner(owner));
        return "owner/rooms/list";
    }

    @GetMapping("/add")
    public String addRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "owner/rooms/form";
    }

    @PostMapping("/add")
    public String addRoomSubmit(@ModelAttribute Room room, Principal principal) {
        User owner = userRepository.findByUsername(principal.getName()).orElseThrow();
        room.setOwner(owner);
        room.setApprovalStatus(ApprovalStatus.PENDING); // Force pending on new post
        roomService.saveRoom(room);
        return "redirect:/owner/rooms";
    }

    @GetMapping("/edit/{id}")
    public String editRoomForm(@PathVariable Long id, Model model) {
        Room room = roomService.getRoomById(id).orElseThrow();
        model.addAttribute("room", room);
        return "owner/rooms/form";
    }

    @PostMapping("/edit/{id}")
    public String editRoomSubmit(@PathVariable Long id, @ModelAttribute Room room, Principal principal) {
        User owner = userRepository.findByUsername(principal.getName()).orElseThrow();
        Room existing = roomService.getRoomById(id).orElseThrow();
        existing.setTitle(room.getTitle());
        existing.setDescription(room.getDescription());
        existing.setPrice(room.getPrice());
        existing.setArea(room.getArea());
        existing.setAddress(room.getAddress());
        // keep old statuses and owner overriden by form
        roomService.saveRoom(existing);
        return "redirect:/owner/rooms";
    }

    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "redirect:/owner/rooms";
    }
}
