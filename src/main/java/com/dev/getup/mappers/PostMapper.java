package com.dev.getup.mappers;

import com.dev.getup.domain.CreatePostRequest;
import com.dev.getup.domain.UpdatePostRequest;
import com.dev.getup.domain.dtos.CreatePostRequestDto;
import com.dev.getup.domain.dtos.PostDto;
import com.dev.getup.domain.dtos.UpdatePostRequestDto;
import com.dev.getup.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto dto);
    @Mapping(target = "status", source = "status")
    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto dto);
}
