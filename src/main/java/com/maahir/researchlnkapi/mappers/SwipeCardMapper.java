package com.maahir.researchlnkapi.mappers;


import com.maahir.researchlnkapi.dtos.swipeCards.PublicSwipeCardDto;
import com.maahir.researchlnkapi.dtos.swipeCards.SwipeCardDto;
import com.maahir.researchlnkapi.dtos.swipeCards.UpdateSwipeCardRequest;
import com.maahir.researchlnkapi.model.entities.SwipeCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SwipeCardMapper {

    @Mapping(source = "profile.profilePicture", target = "profilePicture" )
    SwipeCardDto toDto(SwipeCard swipeCard);

    @Mapping(source = "swipeCard.profile.profilePicture", target = "profilePicture" )
    @Mapping(target = "owner", expression = "java(owner)")
    @Mapping(source = "swipeCard.profile.publicId", target = "publicId")
    PublicSwipeCardDto toDto(SwipeCard swipeCard, boolean owner);

    void update(UpdateSwipeCardRequest request, @MappingTarget SwipeCard swipeCard);
}
