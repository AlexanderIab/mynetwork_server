package com.iablonski.mynetwork.service;

import com.iablonski.mynetwork.entity.User;
import com.iablonski.mynetwork.payload.request.SignUpRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    User loadUserById(Long id);
    User createUser(SignUpRequest userIn);
}
