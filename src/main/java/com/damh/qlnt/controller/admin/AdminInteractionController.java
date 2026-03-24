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
        model.addAttribute("posts", interactionService.getAllActivePosts());
        return "admin/posts/list";
    }

    @PostMapping("/posts/{id}/hide")
    public String hidePost(@PathVariable Long id) {
        interactionService.hidePost(id);
        return "redirect:/admin/interactions/posts";
    }
}
