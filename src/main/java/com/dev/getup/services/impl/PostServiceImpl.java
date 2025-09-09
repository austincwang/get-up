package com.dev.getup.services.impl;

import com.dev.getup.domain.CreatePostRequest;
import com.dev.getup.domain.PostStatus;
import com.dev.getup.domain.UpdatePostRequest;
import com.dev.getup.domain.entities.Category;
import com.dev.getup.domain.entities.Post;
import com.dev.getup.domain.entities.Tag;
import com.dev.getup.domain.entities.User;
import com.dev.getup.repositories.CategoryRepo;
import com.dev.getup.repositories.PostRepo;
import com.dev.getup.services.CategoryService;
import com.dev.getup.services.PostService;
import com.dev.getup.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepo postRepo;
    private final CategoryService categoryService;
    private final TagService tagService;

    private static final int WPM = 200;

    @Override
    public Post getPost(UUID id) {
        return postRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + id + " not found"));
    }

    @Override
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if (categoryId != null && tagId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepo.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED,
                    category,
                    tag
            );
        }
        if (categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            return postRepo.findAllByStatusAndCategory(
                    PostStatus.PUBLISHED,
                    category
            );
        }
        if (tagId != null) {
            Tag tag = tagService.getTagById(tagId);
            return postRepo.findAllByStatusAndTagsContaining(
                    PostStatus.PUBLISHED,
                    tag
            );
        }

        // both are null
        return postRepo.findAllByStatus(
                PostStatus.PUBLISHED
        );
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepo.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Transactional
    @Override
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setAuthor(user);
        newPost.setStatus(createPostRequest.getStatus());
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));

        Category category = categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(category);

        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<Tag> tags = tagService.getTagByIds(tagIds);
        newPost.setTags(new HashSet<>(tags));

        return postRepo.save(newPost);
    }

    @Override
    @Transactional
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {
        Post existingPost = postRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist with id: " + id));
        existingPost.setTitle(updatePostRequest.getTitle());
        existingPost.setContent(updatePostRequest.getContent());
        existingPost.setStatus(updatePostRequest.getStatus());
        existingPost.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));

        UUID updatePostRequestCategoryId = updatePostRequest.getCategoryId();
        if (!existingPost.getCategory().getId().equals(updatePostRequestCategoryId)) {
            Category newCategory = categoryService.getCategoryById(updatePostRequestCategoryId);
            existingPost.setCategory(newCategory);
        }

        Set<UUID> existingTagIds = existingPost.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        Set<UUID> updatePostRequestTagIds = updatePostRequest.getTagIds();
        if (!existingTagIds.equals(updatePostRequestTagIds)) {
            List<Tag> newTags = tagService.getTagByIds(updatePostRequestTagIds);
            existingPost.setTags(new HashSet<>(newTags));
        }

        return postRepo.save(existingPost);
    }

    @Override
    public void deletePost(UUID id) {
        Post post = getPost(id);
        postRepo.delete(post);
    }


    private int calculateReadingTime(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }
        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount / WPM);
    }
}
