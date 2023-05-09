package com.iablonski.mynetwork.repository;

import com.iablonski.mynetwork.entity.Post;
import com.iablonski.mynetwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserOrderByCreatedDesc(User user);

    List<Post> findAllByOrderByCreatedDesc();

    Post findPostByIdAndUser(Long id, User user);

}
