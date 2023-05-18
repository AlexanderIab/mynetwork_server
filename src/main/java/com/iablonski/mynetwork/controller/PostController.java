package com.iablonski.mynetwork.controller;

import com.iablonski.mynetwork.dto.PostDTO;
import com.iablonski.mynetwork.dto.UserDTO;
import com.iablonski.mynetwork.entity.Post;
import com.iablonski.mynetwork.entity.User;
import com.iablonski.mynetwork.facade.PostFacade;
import com.iablonski.mynetwork.payload.response.MessageResponse;
import com.iablonski.mynetwork.service.PostService;
import com.iablonski.mynetwork.validation.ResponseErrorValid;
import jakarta.validation.Valid;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/post")
@CrossOrigin
public class PostController {
    private PostService postService;
    private PostFacade postFacade;
    private ResponseErrorValid responseErrorValid;

    @Autowired
    public PostController(PostService postService, PostFacade postFacade, ResponseErrorValid responseErrorValid) {
        this.postService = postService;
        this.postFacade = postFacade;
        this.responseErrorValid = responseErrorValid;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDTO postDTO,
                                             BindingResult bindingResult,
                                             Principal principal){
        ResponseEntity<Object> errors = responseErrorValid.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        Post post = postService.createPost(postDTO, principal);
        PostDTO newPost = postFacade.postToPostDTO(post);
        return new ResponseEntity<>(newPost, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts(){
        List<PostDTO> postList = postService.getAllPosts().stream()
                .map(postFacade::postToPostDTO).toList();
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    @GetMapping("/user/posts")
    public ResponseEntity<List<PostDTO>> getPostsByUser(Principal principal){
        List<PostDTO> postList = postService.getAllPostsByUser(principal).stream()
                .map(postFacade::postToPostDTO).toList();
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    @PutMapping("/like/{postId}/{username}")
    public ResponseEntity<PostDTO> likePost(@PathVariable("postId") String postId,
                                            @PathVariable("username") String username){
        Post post = postService.likePost(Long.parseLong(postId),username);
        PostDTO postDTO = postFacade.postToPostDTO(post);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") String postId, Principal principal){
        postService.deletePost(Long.parseLong(postId),principal);
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}

