package com.ucomputersa.monolithic.service;

import com.ucomputersa.monolithic.domain.Post;

import java.util.List;

public interface PostService {
    List<Post> getPostsByUserId(String userId);
}
