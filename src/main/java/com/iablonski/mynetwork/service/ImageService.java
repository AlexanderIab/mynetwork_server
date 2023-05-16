package com.iablonski.mynetwork.service;

import com.iablonski.mynetwork.entity.Image;
import com.iablonski.mynetwork.repository.ImageRepository;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;

public interface ImageService {
    <T>Collector<T, ?, T> toSinglePostCollector();
    Image uploadProfileImageToUser(MultipartFile multipartFile, Principal principal) throws IOException;

}
