package com.damh.qlnt.controller.owner;

import com.damh.qlnt.entity.User;
import com.damh.qlnt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/owner/profile")
@RequiredArgsConstructor
public class OwnerProfileController {

    private final UserRepository userRepository;

    @GetMapping
    public String profile(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("user", user);
        return "owner/profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User updatedUser, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        user.setFullName(updatedUser.getFullName());
        user.setPhone(updatedUser.getPhone());
        user.setEmail(updatedUser.getEmail());
        userRepository.save(user);
        return "redirect:/owner/profile?success";
    }
}
