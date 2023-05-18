package com.iablonski.mynetwork.service;

import com.iablonski.mynetwork.dto.UserDTO;
import com.iablonski.mynetwork.entity.User;
import com.iablonski.mynetwork.payload.request.SignUpRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;

public interface UserService {
    User loadUserById(Long id);
    User createUser(SignUpRequest userIn);

    User updateUserProfile(UserDTO userDTO, Principal principal);

    User getUserFromPrincipal(Principal principal);

    User getUserById(Long userId);
}
