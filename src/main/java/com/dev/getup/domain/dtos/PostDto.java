package com.dev.getup.domain.dtos;

import com.dev.getup.domain.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private UUID id;
    private String title;
    private String content;

    // TODO: Author
    private AuthorDto author;
    private CategoryDto category;
    private Set<TagDto> tags;
    private int readingTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private PostStatus status;
}
