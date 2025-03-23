package com.ucomputersa.monolithic.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PostToDelete {
    String postId;
    String userId;
}
