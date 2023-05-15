package com.iablonski.mynetwork.repository;

import com.iablonski.mynetwork.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByUserId(Long id);

    Optional<Image> findByPostId(Long id);

}
