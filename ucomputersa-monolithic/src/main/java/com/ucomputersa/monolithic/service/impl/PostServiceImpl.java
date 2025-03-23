package com.ucomputersa.monolithic.service.impl;

import com.ucomputersa.monolithic.domain.Post;
import com.ucomputersa.monolithic.domain.PostToDelete;
import com.ucomputersa.monolithic.repository.PostRepository;
import com.ucomputersa.monolithic.service.PostService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);

    private PostRepository postRepository;

    public void addPost(Post post) {
        try {
            postRepository.save(post);
        } catch (Exception e) {
            LOGGER.error("Error while saving post, post id: ", post.getId());
            throw e;
        }
    }

    @Override
    public List<Post> getPostsByUserId(String userId) {
        Optional<List<Post>> postsOptional = postRepository.findByUserId(userId);
        return postsOptional.orElseGet(List::of);
    }

    public void deletePost(PostToDelete postToDelete) {
        try {
            Post post = postRepository.findById(postToDelete.getPostId()).orElseThrow();
            if (postToDelete.getUserId().equals(post.getUserId())) {
                postRepository.delete(post);
            }
        } catch (Exception e) {
            LOGGER.error("Error while deleting post, post id: ", postToDelete.getPostId());
            throw e;
        }
    }

    public Post getPostsByPostId(String postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        return postOptional.orElseThrow();
    }
}
