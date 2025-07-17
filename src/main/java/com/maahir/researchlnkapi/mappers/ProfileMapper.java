package com.maahir.researchlnkapi.mappers;

import com.maahir.researchlnkapi.dtos.profiles.ProfileDto;
import com.maahir.researchlnkapi.dtos.profiles.UpdateProfileRequest;
import com.maahir.researchlnkapi.model.entities.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDto toDto(Profile profile);

    void update(UpdateProfileRequest request, @MappingTarget Profile profile);

}
