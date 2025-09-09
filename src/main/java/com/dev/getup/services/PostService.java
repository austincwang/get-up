package com.dev.getup.services;

import com.dev.getup.domain.CreatePostRequest;
import com.dev.getup.domain.UpdatePostRequest;
import com.dev.getup.domain.dtos.CreatePostRequestDto;
import com.dev.getup.domain.entities.Post;
import com.dev.getup.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Post getPost(UUID id);
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest); // does not actually check for user authorization
    void deletePost(UUID id);
}
