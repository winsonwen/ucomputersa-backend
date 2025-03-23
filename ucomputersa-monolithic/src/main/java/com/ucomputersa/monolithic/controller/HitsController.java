package com.ucomputersa.monolithic.controller;

import com.ucomputersa.monolithic.domain.Hits;
import com.ucomputersa.monolithic.service.impl.HitsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/0/hits")
public class HitsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HitsController.class);
    private final HitsServiceImpl hitsService;

    @Autowired
    public HitsController(HitsServiceImpl hitsService) {
        this.hitsService = hitsService;
    }

    @PostMapping
    public ResponseEntity<Hits> postHits(@RequestParam String postId, @RequestParam String userId) {
        if (postId == null || userId == null) {
            return ResponseEntity.badRequest().build();
        }
        Hits hit = new Hits(postId, userId, LocalDateTime.now());
        Hits savedHit = hitsService.saveHit(hit);
        System.out.println(savedHit);
        return ResponseEntity.ok(savedHit);
    }

    @GetMapping("/post")
    public ResponseEntity<Hits> getHitsByPost(@RequestParam String postId) {
        if (postId == null) {
            return ResponseEntity.badRequest().build();
        }

        Hits savedHit = hitsService.getHitsByPostId(postId);
        System.out.println(savedHit);
        return ResponseEntity.ok(savedHit);
    }

    @GetMapping("/hit")
    public ResponseEntity<String> getHitsByHitId(@RequestParam String hitId) {
        if (hitId == null) {
            return ResponseEntity.badRequest().build();
        }

        Hits savedHit = hitsService.getHitsByHitId(hitId);
        if (savedHit == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hits not found.");
        }

        System.out.println(savedHit);
        return ResponseEntity.status(HttpStatus.OK).body("Hits found.");
    }
}
