package com.iablonski.mynetwork.service;

import com.iablonski.mynetwork.dto.PostDTO;
import com.iablonski.mynetwork.entity.Image;
import com.iablonski.mynetwork.entity.Post;
import com.iablonski.mynetwork.entity.User;
import com.iablonski.mynetwork.exception.PostNotFoundException;
import com.iablonski.mynetwork.exception.UserExistException;
import com.iablonski.mynetwork.repository.ImageRepository;
import com.iablonski.mynetwork.repository.PostRepository;
import com.iablonski.mynetwork.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    public static final Logger LOG = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final UserService userService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           ImageRepository imageRepository,
                           UserService userService) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.userService = userService;
    }

    @Override
    public Post createPost(PostDTO postDTO, Principal principal) {
        User user = userService.getUserFromPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setText(postDTO.getText());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
        post.setLikes(0);
        LOG.info("Saving post for User {}", user.getUsername());
        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedDesc();
    }

    @Override
    public Post getPostById(Long postId, Principal principal) {
        User user = userService.getUserFromPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post not found for user: " + user.getUsername()));
    }

    @Override
    public List<Post> getAllPostsByUser(Principal principal) {
        User user = userService.getUserFromPrincipal(principal);
        return postRepository.findAllByUserOrderByCreatedDesc(user);
    }

    @Override
    public Post likePost(Long postId, String username) {
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found for user: " + username));
        // Проверка лайкнул ли пользователь пост
        Optional<String> userLiked = post.getLikedUsers().stream()
                .filter(user -> user.equals(username))
                .findAny();
        // То это дизлайк
        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().remove(username);
            // Если нет, то лайк
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().add(username);
        }
        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId, Principal principal) {
        Post post = getPostById(postId, principal);
        Optional<Image> postImage = imageRepository.findByPostId(post.getId());
        postRepository.delete(post);
        postImage.ifPresent(imageRepository::delete);
    }
}