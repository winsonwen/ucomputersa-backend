package com.ucomputersa.monolithic.domain;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Data
@Document
public class Post {
    @Id
    private String id;
    private String title;
    private String content;
    private List<String> tags = new ArrayList<>();
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private String zone;
    private List<byte[]> photoList;

    @NotNull
    private String userId;
    // TODO
    private Hits hits;
    // TODO
    private List<Comment> comments = new ArrayList<>();
    private List<String> favoriteIds = new ArrayList<>();
    private PostStatus postStatus;
}
