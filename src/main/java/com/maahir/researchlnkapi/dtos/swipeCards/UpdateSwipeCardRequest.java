package com.maahir.researchlnkapi.dtos.swipeCards;

import lombok.Data;

@Data
public class UpdateSwipeCardRequest {
    private String name;
    private String position;
    private String description;
}
