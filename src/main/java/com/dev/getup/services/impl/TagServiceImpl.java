package com.dev.getup.services.impl;

import com.dev.getup.domain.entities.Tag;
import com.dev.getup.repositories.TagRepo;
import com.dev.getup.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepo tagRepo;

    @Override
    public List<Tag> getTags() {
        return tagRepo.findAllWithPostCount();
    }

    @Transactional
    @Override
    public List<Tag> createTags(Set<String> names) {
        List<Tag> existing = tagRepo.findByNameIn(names);
        Set<String> existingNames = existing.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());
        List<Tag> newTags = names.stream().filter(
                name -> !existingNames.contains(name)
        ).map(name -> Tag.builder()
                .name(name)
                .posts(new HashSet<>())
                .build()
        ).toList();

        List<Tag> savedTags = new ArrayList<>();
        if (!newTags.isEmpty()) {
            savedTags = tagRepo.saveAll(newTags);
        }

        savedTags.addAll(existing);

        return savedTags;
    }

    @Override
    public void deleteTag(UUID id) {
        tagRepo.findById(id).ifPresent(tag -> {
            if (!tag.getPosts().isEmpty()) {
                throw new IllegalStateException("Cannot delete tag with posts");
            }
            tagRepo.deleteById(id);
        });
    }

    @Override
    public Tag getTagById(UUID id) {
        return tagRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag with id: " + id + " not found"));
    }

    @Override
    public List<Tag> getTagByIds(Set<UUID> ids) {
        List<Tag> foundIds = tagRepo.findAllById(ids);
        if (foundIds.size() != ids.size()) {
            throw new EntityNotFoundException("Not all specified tag IDS exist");
        }
        return foundIds;
    }
}
