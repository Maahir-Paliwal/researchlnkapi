package com.maahir.researchlnkapi.dtos.posts;

import com.maahir.researchlnkapi.model.enums.ViewType;

import java.time.LocalDateTime;

public class PostDto {
    private Long id;
    private String postType;
    private ViewType viewType;
    private String description;
    private LocalDateTime createdAt;
}
