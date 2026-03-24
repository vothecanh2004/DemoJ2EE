package com.damh.qlnt.controller.auth;

import com.damh.qlnt.dto.UserRegistrationDto;
import com.damh.qlnt.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegistrationDto registrationDto,
                               BindingResult result,
                               Model model) {
        
        if (userService.existsByUsername(registrationDto.getUsername())) {
            result.rejectValue("username", null, "Tên đăng nhập này đã được sử dụng");
        }
        
        if (userService.existsByEmail(registrationDto.getEmail())) {
            result.rejectValue("email", null, "Email này đã được đăng ký tài khoản");
        }

        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.registerNewUser(registrationDto);
            return "redirect:/login?success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }
    
    @GetMapping("/")
    public String home() {
        // Redirection logic can be improved later based on role
        return "index";
    }
}
