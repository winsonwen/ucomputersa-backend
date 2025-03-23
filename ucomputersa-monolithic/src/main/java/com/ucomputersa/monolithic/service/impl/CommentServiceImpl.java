package com.ucomputersa.monolithic.service.impl;

import com.ucomputersa.monolithic.domain.Comment;
import com.ucomputersa.monolithic.repository.CommentRepository;
import com.ucomputersa.monolithic.service.CommentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    private CommentRepository commentRepository;

    public void createComment(Comment comment) {
        commentRepository.save(comment);
    }

    public List<Comment> findCommentsByPostId(String postId) {
        Optional<List<Comment>> commentList = commentRepository.findByPostId(postId);
        return commentList.orElse(null);
    }

    public List<Comment> findRepliesByParentId(String parentId) {
        Optional<List<Comment>> replyList = commentRepository.findByParentId(parentId);
        return replyList.orElse(null);
    }

    public void deleteCommentById(String id) {
        commentRepository.deleteById(id);
    }

    public Optional<Comment> findCommentById(String id) {
        return commentRepository.findById(id);
    }
}
