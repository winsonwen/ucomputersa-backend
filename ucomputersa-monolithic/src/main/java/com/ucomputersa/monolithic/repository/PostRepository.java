package com.ucomputersa.monolithic.repository;

import com.ucomputersa.monolithic.domain.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, String> {

    Optional<Post> findByTitle(String title);

    Optional<List<Post>> findByUserId(String userId);
}
