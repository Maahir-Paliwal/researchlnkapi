package com.maahir.researchlnkapi.mappers;


import com.maahir.researchlnkapi.dtos.users.RegisterUserByOrcid;
import com.maahir.researchlnkapi.dtos.users.RegisterUserByPassword;
import com.maahir.researchlnkapi.dtos.users.UpdateUserRequest;
import com.maahir.researchlnkapi.dtos.users.UserDto;
import com.maahir.researchlnkapi.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(RegisterUserByPassword request);

    User toEntity(RegisterUserByOrcid request);

    void update(UpdateUserRequest request, @MappingTarget User user);

}
