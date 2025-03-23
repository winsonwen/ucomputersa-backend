package com.ucomputersa.monolithic.repository;

import com.ucomputersa.monolithic.domain.Hits;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HitsRepository extends MongoRepository<Hits, String> {
    Hits findByPostId(String postId);
}
