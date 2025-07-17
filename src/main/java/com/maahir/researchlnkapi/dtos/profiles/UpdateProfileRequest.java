package com.maahir.researchlnkapi.dtos.profiles;


import lombok.Data;

//frontend -> backend

@Data
public class UpdateProfileRequest {
    private String name;
    private String position;
    private String description;
    private String profilePicture;
}
