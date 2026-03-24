package com.damh.qlnt.service.impl;

import com.damh.qlnt.entity.*;
import com.damh.qlnt.repository.CommentRepository;
import com.damh.qlnt.repository.MessageRepository;
import com.damh.qlnt.repository.PostLikeRepository;
import com.damh.qlnt.repository.PostRepository;
import com.damh.qlnt.service.InteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InteractionServiceImpl implements InteractionService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final MessageRepository messageRepository;

    @Override
    public Post createPost(Post post) {
        if (post.getType() == PostType.REVIEW) {
            post.setStatus(PostStatus.PENDING);
        } else {
            post.setStatus(PostStatus.ACTIVE);
        }
        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllActivePosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(p -> p.getStatus() == PostStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional
    public void hidePost(Long postId) {
        System.out.println(">>> Hiding Post ID: " + postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.setStatus(PostStatus.HIDDEN);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void unhidePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.setStatus(PostStatus.ACTIVE);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void approvePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.setStatus(PostStatus.ACTIVE);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void rejectPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.setStatus(PostStatus.HIDDEN);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void banPost(Long postId) {
        System.out.println(">>> Banning Post ID: " + postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.setStatus(PostStatus.BANNED);
        postRepository.save(post);
    }

    @Override
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByPost(Post post) {
        return commentRepository.findByPostOrderByCreatedAtAsc(post);
    }

    @Override
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void toggleLike(User user, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        if (postLikeRepository.existsByUserAndPost(user, post)) {
            postLikeRepository.deleteByUserAndPost(user, post);
        } else {
            postLikeRepository.save(PostLike.builder().user(user).post(post).createdAt(LocalDateTime.now()).build());
        }
    }

    @Override
    public long getLikesCount(Post post) {
        return postLikeRepository.countByPost(post);
    }

    @Override
    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getChatHistory(User user1, User user2) {
        // Chat history involves messages where either user1 is sender & user2 receiver, or vice versa
        // Let's filter in memory since we fetch Or match (which includes all their messages essentially, we need AND)
        // Wait, the repository method is findBySenderOrReceiverOrderBySentAtDesc which is not ideal for this specific a-to-b chat
        // Better to fetch all messages involving user1 as sender/receiver, then filter for user2
        List<Message> user1Msgs = messageRepository.findBySenderOrReceiverOrderBySentAtDesc(user1, user1);
        return user1Msgs.stream()
                .filter(m -> m.getSender().getId().equals(user2.getId()) || m.getReceiver().getId().equals(user2.getId()))
                .collect(Collectors.toList());
    }
}
