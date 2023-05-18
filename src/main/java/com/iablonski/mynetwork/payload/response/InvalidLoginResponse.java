package com.iablonski.mynetwork.payload.response;

import com.iablonski.mynetwork.dto.UserDTO;
import com.iablonski.mynetwork.entity.User;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

@Getter
public class InvalidLoginResponse {
    private String username;
    private String password;

    public InvalidLoginResponse() {
        this.username = "Invalid username";
        this.password = "Invalid password";
    }
}
