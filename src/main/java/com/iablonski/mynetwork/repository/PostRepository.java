package com.iablonski.mynetwork.repository;

import com.iablonski.mynetwork.entity.Post;
import com.iablonski.mynetwork.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserOrderByCreatedDesc(User user);

    List<Post> findAllByOrderByCreatedDesc();

    Optional<Post> findPostByIdAndUser(Long id, User user);

    Optional<Post> findPostById(Long id);
}