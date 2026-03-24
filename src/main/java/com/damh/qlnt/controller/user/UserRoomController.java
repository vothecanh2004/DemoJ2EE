package com.damh.qlnt.controller.user;

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
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user/rooms")
@RequiredArgsConstructor
public class UserRoomController {

    private final RoomService roomService;
    private final UserRepository userRepository;

    @GetMapping
    public String searchRooms(@RequestParam(value = "kw", required = false) String kw,
                               @RequestParam(value = "minP", required = false) Double minP,
                               @RequestParam(value = "maxP", required = false) Double maxP,
                               @RequestParam(value = "minA", required = false) Double minA,
                               Model model) {
        List<Room> rooms = roomService.searchRooms(kw, minP, maxP, minA);
        model.addAttribute("rooms", rooms);
        model.addAttribute("kw", kw);
        model.addAttribute("minP", minP);
        model.addAttribute("maxP", maxP);
        model.addAttribute("minA", minA);
        return "user/rooms/list";
    }

    @GetMapping("/{id}")
    public String roomDetail(@PathVariable("id") Long id, Model model, Principal principal) {
        Room room = roomService.getRoomById(id).orElseThrow();
        model.addAttribute("room", room);
        
        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName()).orElse(null);
            if (user != null) {
                boolean isFavorite = roomService.getFavoriteRooms(user).stream().anyMatch(r -> r.getId().equals(id));
                model.addAttribute("isFavorite", isFavorite);
            }
        }
        return "user/rooms/detail";
    }

    @PostMapping("/{id}/favorite")
    public String toggleFavorite(@PathVariable("id") Long id, Principal principal) {
        if (principal == null) return "redirect:/login";
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        roomService.toggleFavorite(user, id);
        return "redirect:/user/rooms/" + id;
    }

    @GetMapping("/favorites")
    public String listFavorites(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("rooms", roomService.getFavoriteRooms(user));
        return "user/rooms/favorites";
    }
}
