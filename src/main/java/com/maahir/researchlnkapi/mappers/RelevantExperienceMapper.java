package com.maahir.researchlnkapi.mappers;

import com.maahir.researchlnkapi.dtos.relevantExperiences.RelevantExperienceDto;
import com.maahir.researchlnkapi.model.entities.RelevantExperience;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RelevantExperienceMapper {

    @Mapping(target ="startAt", expression = "java(formatYearMonth(relevantExperience.getStartAt()))")
    @Mapping(target ="endAt", expression = "java(formatYearMonth(relevantExperience.getEndAt()))")
    RelevantExperienceDto toDto(RelevantExperience relevantExperience);

    List<RelevantExperienceDto> toDtoList(List<RelevantExperience> relevantExperiences);

    default String formatYearMonth(LocalDateTime ts) {
        if (ts == null) return null;
        return DateTimeFormatter.ofPattern("yyyy-MM").format(ts);
    }
}
