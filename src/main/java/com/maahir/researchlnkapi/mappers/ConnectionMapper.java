package com.maahir.researchlnkapi.mappers;

import com.maahir.researchlnkapi.dtos.connections.ConnectionStatusDto;
import com.maahir.researchlnkapi.model.entities.Connection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ConnectionMapper {

    @Mappings({
            @Mapping(target = "connectionStatus", source = "connectionStatus"),
            @Mapping(target = "requesterName", source = "requester.name"),
            @Mapping(target = "requesterPosition", source = "requester.position"),
            @Mapping(target = "requesterProfilePicture", source = "requester.profilePicture"),
            @Mapping(target = "requesterPublicId", source = "requester.publicId")
    })
    ConnectionStatusDto toDto(Connection connection);

}
