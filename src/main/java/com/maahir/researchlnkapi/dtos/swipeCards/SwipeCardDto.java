package com.maahir.researchlnkapi.dtos.swipeCards;

import lombok.Data;

@Data
public class SwipeCardDto {
    private Long id;
    private String name;
    private String position;
    private String description;
    private String profilePicture;
}
