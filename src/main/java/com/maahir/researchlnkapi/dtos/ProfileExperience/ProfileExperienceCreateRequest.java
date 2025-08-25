package com.maahir.researchlnkapi.dtos.ProfileExperience;

import lombok.Data;

@Data
public class ProfileExperienceCreateRequest {
    private String title;
    private String startAt;
    private String endAt;
    private String description;
}
