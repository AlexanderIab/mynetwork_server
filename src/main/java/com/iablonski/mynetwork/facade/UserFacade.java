package com.iablonski.mynetwork.facade;

import com.iablonski.mynetwork.dto.UserDTO;
import com.iablonski.mynetwork.entity.User;

import org.springframework.stereotype.Component;

@Component
public class UserFacade {
    public UserDTO userToUserDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .bio(user.getBio())
                .build();
    }
}