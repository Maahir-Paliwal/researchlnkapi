package com.maahir.researchlnkapi.mappers;

import com.maahir.researchlnkapi.dtos.posts.PostCreateRequest;
import com.maahir.researchlnkapi.dtos.posts.PostDto;
import com.maahir.researchlnkapi.dtos.posts.PostUpdateRequest;
import com.maahir.researchlnkapi.model.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "createdAt", expression = "java(post.getCreatedAt() != null ? post.getCreatedAt().toString() : null)")
    PostDto toDto(Post post);

    List<PostDto> toDtoList(List<Post> posts);

    Post toEntity(PostCreateRequest postCreateRequest);

    void update(PostUpdateRequest postUpdateRequest, @MappingTarget Post post);
}
