package com.maahir.researchlnkapi.mappers;

import com.maahir.researchlnkapi.dtos.connections.ConnectionStatusDto;
import com.maahir.researchlnkapi.model.entities.Connection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConnectionMapper {

    @Mapping(target = "connectionStatus", source = "connectionStatus")
    ConnectionStatusDto toDto(Connection connection);

}
