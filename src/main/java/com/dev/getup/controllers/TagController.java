package com.dev.getup.controllers;

import com.dev.getup.domain.dtos.CreateTagsRequest;
import com.dev.getup.domain.dtos.TagDto;
import com.dev.getup.domain.entities.Tag;
import com.dev.getup.mappers.TagMapper;
import com.dev.getup.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<Tag> tags = tagService.getTags();
        List<TagDto> resps = tags.stream().map(tagMapper::toTagResponse).toList();
        return ResponseEntity.ok(resps);
    }

    @PostMapping
    public ResponseEntity<List<TagDto>> createTags(@RequestBody CreateTagsRequest req) {
        List<Tag> savedTags = tagService.createTags(req.getNames());
        List<TagDto> resp = savedTags.stream().map(tagMapper::toTagResponse).toList();
        return new ResponseEntity<>(
                resp,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
