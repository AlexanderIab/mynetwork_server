package com.iablonski.mynetwork.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    private String username;
    private String bio;

}
