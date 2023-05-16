package com.iablonski.mynetwork.dto;

import com.iablonski.mynetwork.entity.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private String username;
    @NotBlank
    private String message;
}
