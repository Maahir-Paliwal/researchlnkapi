package com.maahir.researchlnkapi.dtos.profileExperience;

import lombok.Data;

@Data
public class ProfileExperienceCreateRequest {
    private String title;
    private String startAt;
    private String endAt;
    private String description;
}
