package com.iablonski.mynetwork.service;

import com.iablonski.mynetwork.dto.ImageDTO;
import com.iablonski.mynetwork.entity.Image;
import com.iablonski.mynetwork.entity.User;
import com.iablonski.mynetwork.exception.PostNotFoundException;
import com.iablonski.mynetwork.repository.CommentRepository;
import com.iablonski.mynetwork.repository.ImageRepository;
import com.iablonski.mynetwork.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageServiceImpl implements ImageService {
    public static final Logger LOG = LoggerFactory.getLogger(ImageServiceImpl.class);

    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository,
                            PostRepository postRepository,
                            UserService userService) {
        this.imageRepository = imageRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    private byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            LOG.error("Cannot compress bytes");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            LOG.error("Cannot decompress bytes");
        }
        return outputStream.toByteArray();
    }

    @Override
    public <T> Collector<T, ?, T> toSinglePostCollector() {
        return Collectors.collectingAndThen(Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }

    @Override
    public Image uploadProfileImageToUser(MultipartFile multipartFile, Principal principal) throws IOException {
        User user = userService.getUserFromPrincipal(principal);
        LOG.info("Uploading profile image to User {}", user.getUsername());
        Image profileImage = imageRepository.findByUserId(user.getId())
                .orElse(null);
        if(!ObjectUtils.isEmpty(profileImage)) imageRepository.delete(profileImage);
        Image image = new Image();
        image.setUserId(user.getId());
        image.setImage(compressImage(multipartFile.getBytes()));
        image.setName(multipartFile.getOriginalFilename());
        return imageRepository.save(image);
    }
}
