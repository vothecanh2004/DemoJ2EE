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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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
    public String addRoomSubmit(@ModelAttribute Room room, 
                               @RequestParam("imageFile") MultipartFile imageFile,
                               Principal principal) throws IOException {
        User owner = userRepository.findByUsername(principal.getName()).orElseThrow();
        room.setOwner(owner);
        room.setApprovalStatus(ApprovalStatus.PENDING);
        
        if (!imageFile.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName));
            room.setImageUrl("/uploads/" + fileName);
        }
        
        roomService.saveRoom(room);
        return "redirect:/owner/rooms";
    }

    @GetMapping("/edit/{id}")
    public String editRoomForm(@PathVariable("id") Long id, Model model) {
        Room room = roomService.getRoomById(id).orElseThrow();
        model.addAttribute("room", room);
        return "owner/rooms/form";
    }

    @PostMapping("/edit/{id}")
    public String editRoomSubmit(@PathVariable("id") Long id, 
                                 @ModelAttribute Room room, 
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 Principal principal) throws IOException {
        User owner = userRepository.findByUsername(principal.getName()).orElseThrow();
        Room existing = roomService.getRoomById(id).orElseThrow();
        existing.setTitle(room.getTitle());
        existing.setDescription(room.getDescription());
        existing.setPrice(room.getPrice());
        existing.setArea(room.getArea());
        existing.setAddress(room.getAddress());

        if (!imageFile.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName));
            existing.setImageUrl("/uploads/" + fileName);
        }

        roomService.saveRoom(existing);
        return "redirect:/owner/rooms";
    }

    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable("id") Long id, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            roomService.deleteRoom(id);
            redirectAttributes.addFlashAttribute("success", "Đã xóa phòng thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa phòng: " + e.getMessage());
        }
        return "redirect:/owner/rooms";
    }
}
