package com.ucomputersa.monolithic.repository;

import com.ucomputersa.monolithic.domain.Favorites;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends MongoRepository<Favorites, String> {
    List<Favorites> findByUserId(String userId);
    List<Favorites> findByPostId(String postId);
    Optional<Favorites> findByUserIdAndPostId(String userId, String postId);
    void deleteByUserIdAndPostId(String userId, String postId);
}
