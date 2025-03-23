package com.ucomputersa.monolithic.controller;

import com.ucomputersa.monolithic.domain.Post;
import com.ucomputersa.monolithic.domain.PostToDelete;
import com.ucomputersa.monolithic.domain.User;
import com.ucomputersa.monolithic.service.impl.PostServiceImpl;
import com.ucomputersa.monolithic.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/0/posts")
public class PostController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);
    private final PostServiceImpl postService;
    private final UserServiceImpl userService;

    @Autowired
    public PostController(PostServiceImpl postService, UserServiceImpl userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<Post> createPost(@RequestBody Post post, @PathVariable("userId") String userId) {
        if (Objects.nonNull(userId) && Objects.nonNull(post) && Objects.nonNull(post.getUserId())) {
            if (post.getUserId().equals(userId)) {
                post.setUserId(userId);
                postService.addPost(post);

                User user = userService.getUserById(userId);
                if (Objects.isNull(user)) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                List<String> postIds = Objects.nonNull(user.getPostIds()) ? user.getPostIds() : new ArrayList<>();
                postIds.add(post.getId());
                user.setPostIds(postIds);
                userService.createUser(user);
                return ResponseEntity.ok(post);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable("userId") String userId) {
        List<Post> posts = new ArrayList<>();
        if (Objects.nonNull(userId)) {
            posts = postService.getPostsByUserId(userId);
            System.out.println(posts);

            return ResponseEntity.ok(posts);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostsByPostId(@PathVariable("postId") String postId) {
        Post posts = null;
        if (Objects.nonNull(postId)) {
            posts = postService.getPostsByPostId(postId);
            // TODO: ADD hitUserIds and

            return ResponseEntity.ok(posts);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deletePosts(@RequestBody PostToDelete postToDelete) {
        if (Objects.nonNull(postToDelete) && Objects.nonNull(postToDelete.getUserId()) && Objects.nonNull(postToDelete.getPostId())) {
            postService.deletePost(postToDelete);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
