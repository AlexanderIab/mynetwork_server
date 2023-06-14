package com.iablonski.mynetwork.controller;

import com.iablonski.mynetwork.entity.Image;
import com.iablonski.mynetwork.payload.response.MessageResponse;
import com.iablonski.mynetwork.service.ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("api/image")
@CrossOrigin
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam("file") MultipartFile multipartFile,
                                                             Principal principal) throws IOException {
        imageService.uploadProfileImageToUser(multipartFile, principal);
        return ResponseEntity.ok(new MessageResponse("Image uploaded successfully"));
    }

    @PostMapping("/upload/{postId}")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable("postId") String postId,
                                                             @RequestParam("file") MultipartFile multipartFile,
                                                             Principal principal) throws IOException {
        imageService.uploadImageToPost(multipartFile, principal, Long.parseLong(postId));
        return ResponseEntity.ok(new MessageResponse("Image uploaded successfully"));
    }

    @GetMapping("/profileImage")
    public ResponseEntity<Image> getImageToUserProfile(Principal principal) {
        Image image = imageService.getImageToUser(principal);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @GetMapping("/image/{postId}")
    public ResponseEntity<Image> getImageToPost(@PathVariable("postId") String postId) {
        Image image = imageService.getImageToPost(Long.parseLong(postId));
        return new ResponseEntity<>(image, HttpStatus.OK);
    }
}