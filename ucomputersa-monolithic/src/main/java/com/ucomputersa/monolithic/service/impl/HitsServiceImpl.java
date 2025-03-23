package com.ucomputersa.monolithic.service.impl;

import com.ucomputersa.monolithic.domain.Hits;
import com.ucomputersa.monolithic.repository.HitsRepository;
import com.ucomputersa.monolithic.service.HitsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class HitsServiceImpl implements HitsService {
    private final HitsRepository hitsRepository;

    public Hits saveHit(Hits hit) {
        return hitsRepository.save(hit);
    }

    public Hits getHitsByPostId(String postId) {
        return hitsRepository.findByPostId(postId);
    }

    public Hits getHitsByHitId(String hitId) {
        Optional<Hits> hit = hitsRepository.findById(hitId);
        return hit.orElse(null);
    }
}
