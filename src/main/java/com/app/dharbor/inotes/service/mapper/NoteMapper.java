package com.app.dharbor.inotes.service.mapper;

import com.app.dharbor.inotes.domain.entity.NoteEntity;
import com.app.dharbor.inotes.domain.entity.UserEntity;
import com.app.dharbor.inotes.dto.NoteDTO;

import org.springframework.stereotype.Component;

@Component
public class NoteMapper implements CustomMapper<NoteDTO, NoteEntity> {
    @Override
    public NoteEntity toEntity(NoteDTO noteDTO) {
        return NoteEntity.builder()
                .id(noteDTO.getId())
                .title(noteDTO.getTitle())
                .content(noteDTO.getContent())
                .userId(noteDTO.getUserId())
                .isArchived(noteDTO.isArchived())
                .build();
    }

    @Override
    public NoteDTO toDTO(NoteEntity noteEntity) {
        return NoteDTO.builder()
                .id(noteEntity.getId())
                .title(noteEntity.getTitle())
                .content(noteEntity.getContent())
                .userId(noteEntity.getUserId())
                .isArchived(noteEntity.isArchived())
                .build();
    }
}
