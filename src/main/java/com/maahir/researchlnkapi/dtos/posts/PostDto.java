package com.maahir.researchlnkapi.dtos.posts;

import com.maahir.researchlnkapi.model.enums.ViewType;
import lombok.Data;

@Data
public class PostDto {
    private Long id;
    private String postType;
    private ViewType viewType;
    private String description;
    private String createdAt;
}
