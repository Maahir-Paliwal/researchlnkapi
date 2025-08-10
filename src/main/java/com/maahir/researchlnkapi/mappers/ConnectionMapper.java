package com.maahir.researchlnkapi.mappers;

import com.maahir.researchlnkapi.dtos.connections.ConnectionStatusDto;
import com.maahir.researchlnkapi.model.entities.Connection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConnectionMapper {

    ConnectionStatusDto toDto(Connection connection);


}
