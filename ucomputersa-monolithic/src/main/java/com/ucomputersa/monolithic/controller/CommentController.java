package com.ucomputersa.monolithic.controller;

import com.ucomputersa.monolithic.domain.Comment;
import com.ucomputersa.monolithic.service.impl.CommentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/0/comments")
public class CommentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);
    private final CommentServiceImpl commentService;

    @Autowired
    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        if (Objects.nonNull(comment) && Objects.nonNull(comment.getUserId())) {
            System.out.println(comment); // test
            commentService.createComment(comment);
            return ResponseEntity.ok(comment);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Get all comments for a post (only top-level comments)
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable String postId) {
        List<Comment> comments = commentService.findCommentsByPostId(postId);
        return comments != null ? ResponseEntity.ok(comments) : ResponseEntity.notFound().build();
    }

    // Get replies to a specific comment
    @GetMapping("/replies/{parentId}")
    public ResponseEntity<List<Comment>> getReplies(@PathVariable String parentId) {
        List<Comment> replies = commentService.findRepliesByParentId(parentId);
        return replies != null ? ResponseEntity.ok(replies) : ResponseEntity.notFound().build();
    }

    // Delete a comment
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable String id) {
        Optional<Comment> commentOptional = commentService.findCommentById(id);
        if (commentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found.");
        }

        Comment comment = commentOptional.get();
        // Prevent deleting replies
        if (comment.getParentId() != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Replies cannot be deleted.");
        }

        // Delete top-level comment
        commentService.deleteCommentById(id);
        return ResponseEntity.noContent().build();
    }
}
