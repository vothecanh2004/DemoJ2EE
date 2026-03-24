package com.damh.qlnt.controller.admin;

import com.damh.qlnt.service.InteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/interactions")
@RequiredArgsConstructor
public class AdminInteractionController {

    private final InteractionService interactionService;

    @GetMapping("/posts")
    public String listPosts(Model model) {
        model.addAttribute("posts", interactionService.getAllPosts());
        return "admin/posts/list";
    }

    @PostMapping("/posts/{id}/hide")
    public String hidePost(@PathVariable("id") Long id) {
        System.out.println(">>> Controller: Hiding Post ID: " + id);
        interactionService.hidePost(id);
        return "redirect:/admin/interactions/posts";
    }

    @PostMapping("/posts/{id}/unhide")
    public String unhidePost(@PathVariable("id") Long id) {
        System.out.println(">>> Controller: Unhiding Post ID: " + id);
        interactionService.unhidePost(id);
        return "redirect:/admin/interactions/posts";
    }

    @PostMapping("/posts/{id}/approve")
    public String approvePost(@PathVariable("id") Long id) {
        interactionService.approvePost(id);
        return "redirect:/admin/interactions/posts";
    }

    @PostMapping("/posts/{id}/reject")
    public String rejectPost(@PathVariable("id") Long id) {
        interactionService.rejectPost(id);
        return "redirect:/admin/interactions/posts";
    }

    @PostMapping("/posts/{id}/ban")
    public String banPost(@PathVariable("id") Long id) {
        System.out.println(">>> Controller: Banning Post ID: " + id);
        interactionService.banPost(id);
        return "redirect:/admin/interactions/posts";
    }
}
