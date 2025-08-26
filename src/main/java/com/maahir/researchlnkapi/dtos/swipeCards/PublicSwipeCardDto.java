package com.maahir.researchlnkapi.dtos.swipeCards;

import lombok.Data;

@Data
public class PublicSwipeCardDto {
    private Long id;
    private String publicId;
    private String name;
    private String position;
    private String description;
    private String profilePicture;

    private boolean owner;
}
