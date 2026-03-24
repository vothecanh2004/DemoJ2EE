package com.damh.qlnt.controller.user;

import com.damh.qlnt.entity.Comment;
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
@RequestMapping("/user/interactions")
@RequiredArgsConstructor
public class UserInteractionController {

    private final InteractionService interactionService;
    private final UserRepository userRepository;

    @GetMapping("/posts")
    public String feed(Model model) {
        model.addAttribute("posts", interactionService.getAllActivePosts());
        return "user/posts/feed";
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
        return "redirect:/user/interactions/posts";
    }

    @PostMapping("/posts/{postId}/like")
    public String likePost(@PathVariable Long postId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        interactionService.toggleLike(user, postId);
        return "redirect:/user/interactions/posts";
    }

    @PostMapping("/posts/{postId}/comments")
    public String addComment(@PathVariable Long postId, @RequestParam String content, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Post post = interactionService.getPostById(postId).orElseThrow();
        
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
                
        interactionService.addComment(comment);
        return "redirect:/user/interactions/posts";
    }
}
