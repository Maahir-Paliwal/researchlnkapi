package com.maahir.researchlnkapi.mappers;

import com.maahir.researchlnkapi.dtos.profileExperience.ProfileExperienceDto;
import com.maahir.researchlnkapi.model.entities.ProfileExperience;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileExperienceMapper {

    @Mapping(target ="startAt", expression = "java(formatYearMonth(profileExperience.getStartAt()))")
    @Mapping(target ="endAt", expression = "java(formatYearMonth(profileExperience.getEndAt()))")
    ProfileExperienceDto toDto(ProfileExperience profileExperience);

    List<ProfileExperienceDto> toDtoList(List<ProfileExperience> profileExperiences);

    // ---------------- HELPER ------------------
    default String formatYearMonth(LocalDateTime ts) {
        if (ts == null) return null;
        return DateTimeFormatter.ofPattern("yyyy-MM").format(ts);
    }
}
