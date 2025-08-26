package com.maahir.researchlnkapi.dtos.profileExperience;

import lombok.Data;

@Data
public class ProfileExperienceDto {
    private Long id;
    private String title;
    private String startAt;
    private String endAt;
    private String description;

}
