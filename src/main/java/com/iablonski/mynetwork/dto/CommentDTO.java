package com.iablonski.mynetwork.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDTO {
    private Long id;
    private String username;
    @NotBlank
    private String message;
}
