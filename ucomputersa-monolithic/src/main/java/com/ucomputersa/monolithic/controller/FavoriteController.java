package com.ucomputersa.monolithic.controller;

import com.ucomputersa.monolithic.domain.Favorites;
import com.ucomputersa.monolithic.domain.Post;
import com.ucomputersa.monolithic.domain.User;
import com.ucomputersa.monolithic.service.impl.FavoriteServiceImpl;
import com.ucomputersa.monolithic.service.impl.PostServiceImpl;
import com.ucomputersa.monolithic.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/0/favorite")
public class FavoriteController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteController.class);
    private final FavoriteServiceImpl favoriteService;
    private final UserServiceImpl userService;
    private final PostServiceImpl postService;

    @Autowired
    public FavoriteController(FavoriteServiceImpl favoriteService, UserServiceImpl userService, PostServiceImpl postService) {
        this.favoriteService = favoriteService;
        this.userService = userService;
        this.postService = postService;
    }

    // Add a Favorite
    @PostMapping
    public ResponseEntity<Favorites> addFavorite(@RequestParam String postId, @RequestParam String userId) {
        if (postId == null || userId == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Optional<Favorites> existingFavorite = favoriteService.findByUserIdAndPostId(userId, postId);
        if (existingFavorite.isPresent()) {
            return ResponseEntity.badRequest().body(existingFavorite.get());
        }

        User user = userService.getUserById(userId);
        Post post = postService.getPostsByPostId(postId);
//        Optional<Favorites> favorite = favoriteService.findByUserIdAndPostId(userId, postId);
        if (Objects.isNull(user) || Objects.isNull(post)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<String> favoritesIdFromUser = Objects.isNull(user.getFavoriteIds()) ? new ArrayList<>() : user.getFavoriteIds();
        favoritesIdFromUser.add(postId);
        user.setFavoriteIds(favoritesIdFromUser);
        userService.createUser(user);

        List<String> favoritesIdFromPost = Objects.isNull(post.getFavoriteIds()) ? new ArrayList<>() : user.getFavoriteIds();
        favoritesIdFromPost.add(postId);
        post.setFavoriteIds(favoritesIdFromPost);
        postService.addPost(post);

        Favorites favorite = new Favorites(postId, userId, LocalDateTime.now());
        return ResponseEntity.ok(favoriteService.save(favorite));
    }
}