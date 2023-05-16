package com.iablonski.mynetwork.service;

import com.iablonski.mynetwork.dto.CommentDTO;
import com.iablonski.mynetwork.entity.Comment;
import com.iablonski.mynetwork.entity.Post;
import com.iablonski.mynetwork.entity.User;
import com.iablonski.mynetwork.exception.PostNotFoundException;
import com.iablonski.mynetwork.repository.CommentRepository;
import com.iablonski.mynetwork.repository.PostRepository;
import com.iablonski.mynetwork.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    public static final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository,
                              UserService userService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }


    @Override
    public Comment saveComment(Long postId, CommentDTO commentDTO, Principal principal) {
        User user = userService.getUserFromPrincipal(principal);
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found for user: " + user.getUsername()));
        Comment comment = new Comment();
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDTO.getMessage());
        comment.setPost(post);
        comment.setUserId(user.getId());

        LOG.info("Saving comment for Post {}", post.getId());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        return commentRepository.findAllByPost(post);
    }

    @Override
    public void deleteComment(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }
}
