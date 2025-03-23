package com.ucomputersa.monolithic.domain;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private String id;
    private String content;
    private String userId;
    private String postId;
    private String parentId;
    private Date timestamp = new Date();
}
