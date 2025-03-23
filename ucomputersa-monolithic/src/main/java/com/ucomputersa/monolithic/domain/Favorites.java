package com.ucomputersa.monolithic.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Favorites {
    @Id
    private String id;
    private String postId;
    private String userId;
    private LocalDateTime createdAt;

    public Favorites(String postId, String userId, LocalDateTime now) {
        this.postId = postId;
        this.userId = userId;
        this.createdAt = now;
    }
}
