package com.iablonski.mynetwork.facade;

import com.iablonski.mynetwork.dto.PostDTO;
import com.iablonski.mynetwork.entity.Post;

import org.springframework.stereotype.Component;

@Component
public class PostFacade {
    public PostDTO postToPostDTO(Post post){
        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .text(post.getText())
                .likes(post.getLikes())
                .usersLiked(post.getLikedUsers())
                .location(post.getLocation())
                .username(post.getUser().getUsername())
                .build();
    }
}