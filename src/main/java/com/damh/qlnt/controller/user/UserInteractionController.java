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
    public String feed(Model model, Principal principal) {
        model.addAttribute("posts", interactionService.getAllActivePosts());
        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName()).orElseThrow();
            model.addAttribute("currentUser", user);
        }
        return "user/posts/feed";
    }

    @PostMapping("/posts")
    public String createPost(@RequestParam("content") String content, @RequestParam("type") PostType type, Principal principal) {
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
    public String likePost(@PathVariable("postId") Long postId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        interactionService.toggleLike(user, postId);
        return "redirect:/user/interactions/posts";
    }

    @PostMapping("/posts/{postId}/comments")
    public String addComment(@PathVariable("postId") Long postId, @RequestParam("content") String content, 
                             @RequestParam(value = "parentId", required = false) Long parentId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Post post = interactionService.getPostById(postId).orElseThrow();
        
        Comment.CommentBuilder builder = Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .createdAt(LocalDateTime.now());
                
        if (parentId != null) {
            Comment parent = interactionService.getCommentById(parentId).orElseThrow();
            builder.parent(parent);
        }
                
        interactionService.addComment(builder.build());
        return "redirect:/user/interactions/posts";
    }
}
