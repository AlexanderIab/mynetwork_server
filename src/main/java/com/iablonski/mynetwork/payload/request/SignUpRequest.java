package com.iablonski.mynetwork.payload.request;

import com.iablonski.mynetwork.validation.MatchingPasswords;
import com.iablonski.mynetwork.validation.ValidEmail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
@MatchingPasswords
public class SignUpRequest {
    @Email(message = "Email format: xxxx@xxxx.xxx")
    @NotBlank(message = "Email cannot be empty")
    @ValidEmail
    private String email;
    @NotBlank(message = "Firstname cannot be empty")
    private String firstname;
    @NotBlank(message = "Lastname cannot be empty")
    private String lastname;
    @NotBlank(message = "Username cannot be empty")
    private String username;
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 4)
    private String password;
    private String passwordConfirmation;
}