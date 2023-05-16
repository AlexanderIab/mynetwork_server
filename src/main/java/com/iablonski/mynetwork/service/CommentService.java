package com.iablonski.mynetwork.service;

import com.iablonski.mynetwork.dto.CommentDTO;
import com.iablonski.mynetwork.entity.Comment;

import java.security.Principal;
import java.util.List;

public interface CommentService {
    Comment saveComment(Long postId, CommentDTO commentDTO, Principal principal);

    List<Comment> getAllCommentsForPost(Long postId);

    void deleteComment(Long commentId);
}
