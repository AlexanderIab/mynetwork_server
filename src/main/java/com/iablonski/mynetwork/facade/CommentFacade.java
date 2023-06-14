package com.iablonski.mynetwork.facade;

import com.iablonski.mynetwork.dto.CommentDTO;
import com.iablonski.mynetwork.entity.Comment;

import org.springframework.stereotype.Component;

@Component
public class CommentFacade {
    public CommentDTO commentToCommentDTO(Comment comment){
        return CommentDTO.builder()
                .id(comment.getId())
                .username(comment.getUsername())
                .message(comment.getMessage())
                .build();
    }
}