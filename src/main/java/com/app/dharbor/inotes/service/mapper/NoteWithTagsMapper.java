package com.app.dharbor.inotes.service.mapper;

import com.app.dharbor.inotes.domain.entity.NoteEntity;
import com.app.dharbor.inotes.domain.entity.TagEntity;
import com.app.dharbor.inotes.dto.NoteWithTagsDTO;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class NoteWithTagsMapper implements CustomMapper<NoteWithTagsDTO, NoteEntity> {

    @Override
    public NoteWithTagsDTO toDTO(NoteEntity noteEntity) {
        if (noteEntity == null) {
            return null;
        }

        return NoteWithTagsDTO.builder()
                .id(noteEntity.getId())
                .userId(noteEntity.getUserId())
                .title(noteEntity.getTitle())
                .content(noteEntity.getContent())
                .isArchived(noteEntity.isArchived())
                .tagNames(noteEntity.getTags().stream()
                        .map(TagEntity::getName)
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    public NoteEntity toEntity(NoteWithTagsDTO noteWithTags) {
        if (noteWithTags == null) {
            return null;
        }

        NoteEntity noteEntity = NoteEntity.builder()
                .id(noteWithTags.getId())
                .userId(noteWithTags.getUserId())
                .title(noteWithTags.getTitle())
                .content(noteWithTags.getContent())
                .isArchived(noteWithTags.isArchived())
                .build();

        if (noteWithTags.getTagNames() != null) {
            Set<TagEntity> tags = noteWithTags.getTagNames().stream()
                    .map(tagName -> TagEntity.builder().name(tagName).build())
                    .collect(Collectors.toSet());
            noteEntity.setTags(tags);
        }

        return noteEntity;
    }
}
