package com.damh.qlnt.controller.owner;

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

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.security.Principal;
import java.util.Map;
import java.net.URI;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/owner/interactions")
@RequiredArgsConstructor
public class OwnerInteractionController {

    private final InteractionService interactionService;
    private final UserRepository userRepository;

    @GetMapping("/posts")
    public String listPosts(Model model, Principal principal) {
        model.addAttribute("posts", interactionService.getAllActivePosts());
        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName()).orElseThrow();
            model.addAttribute("currentUser", user);
        }
        return "owner/posts/list";
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
        return "redirect:/owner/interactions/posts";
    }

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<Object> likePost(@PathVariable("postId") Long postId, Principal principal, HttpServletRequest request) {
        System.out.println("Owner Like Request - X-Requested-With: " + request.getHeader("X-Requested-With"));
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        interactionService.toggleLike(user, postId);
        
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            Post post = interactionService.getPostById(postId).orElseThrow();
            return ResponseEntity.ok(Map.of("success", true, "likesCount", post.getLikes().size()));
        }
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/owner/interactions/posts")).build();
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Object> addComment(@PathVariable("postId") Long postId, @RequestParam("content") String content, 
                             @RequestParam(value = "parentId", required = false) Long parentId, Principal principal,
                             HttpServletRequest request) {
        System.out.println("Owner Comment Request - X-Requested-With: " + request.getHeader("X-Requested-With"));
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
                
        Comment comment = interactionService.addComment(builder.build());
        
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return ResponseEntity.ok(Map.of(
                "success", true, 
                "username", user.getUsername(),
                "content", comment.getContent(),
                "createdAt", LocalDateTime.now().toString(),
                "commentId", comment.getId()
            ));
        }
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/owner/interactions/posts")).build();
    }
}

