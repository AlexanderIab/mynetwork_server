package com.iablonski.mynetwork.service;

import com.iablonski.mynetwork.dto.UserDTO;
import com.iablonski.mynetwork.entity.Role;
import com.iablonski.mynetwork.entity.User;
import com.iablonski.mynetwork.exception.UserExistException;
import com.iablonski.mynetwork.payload.request.SignUpRequest;
import com.iablonski.mynetwork.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User loadUserById(Long id) {
        return userRepository.findUserById(id).orElse(null);
    }

    @Override
    public User createUser(SignUpRequest userIn) {
        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setUsername(userIn.getUsername());
        user.setFirstname(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.setRole(Role.USER);

        try {
            LOG.info("Saving user {}", userIn.getUsername());
            return userRepository.save(user);
        } catch (Exception exception){
            LOG.error("Error during registration {}", exception.getMessage());
            throw new UserExistException("User with username: " + user.getUsername() + " already exists");
        }
    }

    @Override
    public User updateUserProfile(UserDTO userDTO, Principal principal) {
        User user = getUserFromPrincipal(principal);
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setBio(userDTO.getBio());
        return userRepository.save(user);
    }



    @Override
    public User getUserFromPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name: " + username));
    }

}
