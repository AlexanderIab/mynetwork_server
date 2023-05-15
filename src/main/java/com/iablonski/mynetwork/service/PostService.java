package com.iablonski.mynetwork.service;

import com.iablonski.mynetwork.dto.PostDTO;
import com.iablonski.mynetwork.entity.Post;

import java.security.Principal;
import java.util.List;

public interface PostService {
    Post createPost(PostDTO postDTO, Principal principal);

    List<Post> getAllPosts();

    Post getPostById(Long postId, Principal principal);

    List<Post> getAllPostsByUser(Principal principal);

    Post likePost(Long id, String username);

    void deletePost(Long postId, Principal principal);
}
