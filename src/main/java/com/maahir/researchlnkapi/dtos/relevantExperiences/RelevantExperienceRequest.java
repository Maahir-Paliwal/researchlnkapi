package com.maahir.researchlnkapi.dtos.relevantExperiences;

import lombok.Data;

@Data
public class RelevantExperienceRequest {
    private String title;
    private String startAt;
    private String endAt;
    private String description;
}
