package com.iablonski.mynetwork.repository;

import com.iablonski.mynetwork.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    User findUserByEmail(String email);

    Optional<User> findUserById(Long id);
}