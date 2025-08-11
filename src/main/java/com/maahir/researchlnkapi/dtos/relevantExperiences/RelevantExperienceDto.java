package com.maahir.researchlnkapi.dtos.relevantExperiences;

import lombok.Data;

@Data
public class RelevantExperienceDto {
    private Long id;
    private String title;
    private String startAt;
    private String endAt;
    private String description;
}
