package com.damh.qlnt.controller.owner;

import com.damh.qlnt.entity.Post;
import com.damh.qlnt.entity.PostType;
import com.damh.qlnt.entity.User;
import com.damh.qlnt.repository.UserRepository;
import com.damh.qlnt.service.InteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/owner/interactions")
@RequiredArgsConstructor
public class OwnerInteractionController {

    private final InteractionService interactionService;
    private final UserRepository userRepository;

    @GetMapping("/posts")
    public String listPosts(Model model) {
        model.addAttribute("posts", interactionService.getAllActivePosts());
        return "owner/posts/list";
    }

    @PostMapping("/posts")
    public String createPost(@RequestParam String content, @RequestParam PostType type, Principal principal) {
        User author = userRepository.findByUsername(principal.getName()).orElseThrow();
        Post post = Post.builder()
                .author(author)
                .content(content)
                .type(type)
                .createdAt(LocalDateTime.now())
                .build();
        interactionService.createPost(post);
        return "redirect:/owner/interactions/posts";
    }
}
