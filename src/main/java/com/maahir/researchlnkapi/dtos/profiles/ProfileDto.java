package com.maahir.researchlnkapi.dtos.profiles;


import lombok.Data;

//backend -> frontend

@Data
public class ProfileDto {
    private Long id;
    private String name;
    private String position;
    private String description;
    private String profilePicture;
    private Long createdAt;
    private Long updatedAt;
}
