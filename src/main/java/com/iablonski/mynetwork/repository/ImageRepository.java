package com.iablonski.mynetwork.repository;

import com.iablonski.mynetwork.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByUserId(Long id);

    Image findByPostId(Long id);

}
