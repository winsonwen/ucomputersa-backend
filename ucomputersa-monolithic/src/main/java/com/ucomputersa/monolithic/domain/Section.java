package com.ucomputersa.monolithic.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document
public class Section {
    @Id
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private boolean visible;
}
