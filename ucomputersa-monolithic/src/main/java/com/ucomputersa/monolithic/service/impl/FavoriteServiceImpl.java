package com.ucomputersa.monolithic.service.impl;

import com.ucomputersa.monolithic.domain.Favorites;
import com.ucomputersa.monolithic.repository.FavoriteRepository;
import com.ucomputersa.monolithic.service.FavoriteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;

    public Optional<Favorites> findByUserIdAndPostId(String userId, String postId) {
        return favoriteRepository.findByUserIdAndPostId(userId, postId);
    }

    public Favorites save(Favorites favorite) {
        return favoriteRepository.save(favorite);
    }
}
