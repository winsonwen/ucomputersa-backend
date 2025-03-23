package com.ucomputersa.monolithic.repository;

import com.ucomputersa.monolithic.domain.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, String> {

    Optional<List<Comment>> findByPostId(String postId);
    Optional<List<Comment>> findByParentId(String parentId);
}
