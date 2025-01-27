package com.app.dharbor.inotes.web.rest;

import com.app.dharbor.inotes.common.Path;
import com.app.dharbor.inotes.dto.TagDTO;
import com.app.dharbor.inotes.service.implement.TagServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagController {
    private final TagServiceImpl tagService;

    public TagController(TagServiceImpl tagService) {
        this.tagService = tagService;
    }

    @GetMapping(Path.TAG)
    public ResponseEntity<List<TagDTO>> getTags() {
        return new ResponseEntity<>(this.tagService.findAllByUserAuth(), HttpStatus.OK);
    }

    @PostMapping(Path.TAG)
    public ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        return new ResponseEntity<>(this.tagService.createTag(tagDTO), HttpStatus.CREATED);
    }

    @DeleteMapping(Path.TAG_ID)
    public ResponseEntity<TagDTO> deleteTag(@PathVariable Long id) {
        return new ResponseEntity<>(this.tagService.deleteTag(id), HttpStatus.OK);
    }
}
