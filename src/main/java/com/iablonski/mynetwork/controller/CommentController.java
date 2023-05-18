package com.iablonski.mynetwork.controller;

import com.iablonski.mynetwork.dto.CommentDTO;
import com.iablonski.mynetwork.entity.Comment;
import com.iablonski.mynetwork.facade.CommentFacade;
import com.iablonski.mynetwork.payload.response.MessageResponse;
import com.iablonski.mynetwork.service.CommentService;
import com.iablonski.mynetwork.validation.ResponseErrorValid;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.List;

@RestController
@RequestMapping("api/comment")
@CrossOrigin
public class CommentController {
    private final CommentService commentService;
    private final CommentFacade commentFacade;
    private final ResponseErrorValid responseErrorValid;

    @Autowired
    public CommentController(CommentService commentService,
                             CommentFacade commentFacade,
                             ResponseErrorValid responseErrorValid) {
        this.commentService = commentService;
        this.commentFacade = commentFacade;
        this.responseErrorValid = responseErrorValid;
    }

    @PostMapping("/create/{postId}")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDTO commentDTO,
                                                @PathVariable("postId") String postId,
                                                BindingResult bindingResult,
                                                Principal principal) {
        ResponseEntity<Object> errors = responseErrorValid.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        Comment comment = commentService.saveComment(Long.parseLong(postId), commentDTO, principal);
        CommentDTO newComment = commentFacade.commentToCommentDTO(comment);
        return new ResponseEntity<>(newComment, HttpStatus.OK);
    }

    @GetMapping("/all/{postId}")
    public ResponseEntity<List<CommentDTO>> getAllPostComments(@PathVariable String postId) {
        List<CommentDTO> commentList = commentService.getAllPostComments(Long.parseLong(postId)).stream()
                .map(commentFacade::commentToCommentDTO).toList();
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId) {
        commentService.deleteComment(Long.parseLong(commentId));
        return ResponseEntity.ok(new MessageResponse("Comment was deleted"));
    }
}