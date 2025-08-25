package com.maahir.researchlnkapi.mappers;

import com.maahir.researchlnkapi.dtos.ProfileExperience.ProfileExperienceDto;
import com.maahir.researchlnkapi.dtos.relevantExperiences.RelevantExperienceDto;
import com.maahir.researchlnkapi.model.entities.ProfileExperience;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileExperienceMapper {

    @Mapping(target ="startAt", expression = "java(formatYearMonth(relevantExperience.getStartAt()))")
    @Mapping(target ="endAt", expression = "java(formatYearMonth(relevantExperience.getEndAt()))")
    ProfileExperienceDto toDto(RelevantExperienceDto relevantExperience);

    List<ProfileExperienceDto> toDtoList(List<ProfileExperience> profileExperiences);

    // ---------------- HELPER ------------------
    default String formatYearMonth(LocalDateTime ts) {
        if (ts == null) return null;
        return DateTimeFormatter.ofPattern("yyyy-MM").format(ts);
    }
}
