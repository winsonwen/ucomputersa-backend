package com.ucomputersa.monolithic.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Document
@NoArgsConstructor
public class Hits {
    @Id
    private String id;
    private String postId;
    private String userId;
    private LocalDateTime timestamp;

    public Hits(String postId, String userId, LocalDateTime now) {
        this.postId = postId;
        this.userId = userId;
        this.timestamp = now;
    }
}
