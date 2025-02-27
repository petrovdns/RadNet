package com.petrovdns.radnet.repository;

import com.petrovdns.radnet.entity.Comment;
import com.petrovdns.radnet.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
    List<Comment> findByIdAndUserId(Long postId, Long userId);
}
