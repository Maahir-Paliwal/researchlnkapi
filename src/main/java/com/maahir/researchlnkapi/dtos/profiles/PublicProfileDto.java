package com.maahir.researchlnkapi.dtos.profiles;

import lombok.Data;


//Dto to be returned on ResearchLNK/profile/[publicId]
@Data
public class PublicProfileDto {
    private Long id;
    private String publicId;
    private String name;
    private String position;
    private String description;
    private String profilePicture;

    private boolean isOwner;
}
