package com.damh.qlnt.service;

import com.damh.qlnt.entity.*;
import java.util.List;
import java.util.Optional;

public interface InteractionService {
    // Post actions
    Post createPost(Post post);
    List<Post> getAllActivePosts();
    void hidePost(Long postId);
    Optional<Post> getPostById(Long id);
    
    // Comment actions
    Comment addComment(Comment comment);
    List<Comment> getCommentsByPost(Post post);
    void deleteComment(Long commentId);

    // Like actions
    void toggleLike(User user, Long postId);
    long getLikesCount(Post post);

    // Chat actions
    Message sendMessage(Message message);
    List<Message> getChatHistory(User user1, User user2);
}
