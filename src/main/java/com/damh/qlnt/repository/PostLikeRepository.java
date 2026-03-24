package com.damh.qlnt.repository;

import com.damh.qlnt.entity.Post;
import com.damh.qlnt.entity.PostLike;
import com.damh.qlnt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);
    long countByPost(Post post);
}
