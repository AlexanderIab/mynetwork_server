package com.iablonski.mynetwork.repository;

import com.iablonski.mynetwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User findUserById(Long id);
}
