package com.damh.qlnt.controller;

import com.damh.qlnt.entity.Message;
import com.damh.qlnt.entity.User;
import com.damh.qlnt.repository.UserRepository;
import com.damh.qlnt.service.InteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final InteractionService interactionService;
    private final UserRepository userRepository;

    @GetMapping
    public String listConversations(Model model, Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        // Get all unique users this user has chatted with
        List<Message> userMsgs = interactionService.getChatHistory(currentUser, currentUser); 
        // Note: InteractionService.getChatHistory(u1, u1) returns all msgs involving u1 as sender OR receiver
        
        List<User> chattedUsers = userMsgs.stream()
                .map(m -> m.getSender().getId().equals(currentUser.getId()) ? m.getReceiver() : m.getSender())
                .distinct()
                .collect(Collectors.toList());
                
        model.addAttribute("chattedUsers", chattedUsers);
        model.addAttribute("currentUser", currentUser);
        return "chat/list";
    }

    @GetMapping("/{otherUserId}")
    public String conversation(@PathVariable("otherUserId") Long otherUserId, Model model, Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        User otherUser = userRepository.findById(otherUserId).orElseThrow();
        
        model.addAttribute("messages", interactionService.getChatHistory(currentUser, otherUser));
        model.addAttribute("otherUser", otherUser);
        model.addAttribute("currentUser", currentUser);
        return "chat/conversation";
    }

    @PostMapping("/{otherUserId}/send")
    public String sendMessage(@PathVariable("otherUserId") Long otherUserId, @RequestParam("content") String content, Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        User otherUser = userRepository.findById(otherUserId).orElseThrow();
        
        Message msg = Message.builder()
                .sender(currentUser)
                .receiver(otherUser)
                .content(content)
                .sentAt(LocalDateTime.now())
                .build();
                
        interactionService.sendMessage(msg);
        return "redirect:/chat/" + otherUserId;
    }
}
