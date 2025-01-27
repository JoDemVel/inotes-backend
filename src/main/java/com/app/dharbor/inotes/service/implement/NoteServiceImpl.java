package com.app.dharbor.inotes.service.implement;

import com.app.dharbor.inotes.common.ErrorMessages;
import com.app.dharbor.inotes.domain.entity.NoteEntity;
import com.app.dharbor.inotes.domain.entity.TagEntity;
import com.app.dharbor.inotes.dto.NoteDTO;
import com.app.dharbor.inotes.dto.NoteWithTagsDTO;
import com.app.dharbor.inotes.repository.data.NoteRepository;
import com.app.dharbor.inotes.repository.data.TagRepository;
import com.app.dharbor.inotes.service.NoteService;
import com.app.dharbor.inotes.service.mapper.NoteMapper;
import com.app.dharbor.inotes.service.mapper.NoteWithTagsMapper;
import com.app.dharbor.inotes.utils.UserInfo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

@Service
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final TagRepository tagRepository;
    private final NoteMapper noteMapper;
    private final NoteWithTagsMapper noteWithTagsMapper;
    private final UserInfo userInfo;

    public NoteServiceImpl(NoteRepository noteRepository, TagRepository tagRepository, NoteMapper noteMapper, NoteWithTagsMapper noteWithTagsMapper, UserInfo userInfo) {
        this.noteRepository = noteRepository;
        this.tagRepository = tagRepository;
        this.noteMapper = noteMapper;
        this.noteWithTagsMapper = noteWithTagsMapper;
        this.userInfo = userInfo;
    }

    @Override
    public List<NoteDTO> listNotesByUserId(Long userID, Boolean archived) {
        List<NoteEntity> notes = (archived == null)
                ? noteRepository.findAllNotesByUserId(userID)
                : noteRepository.findAllNotesByUserIdAndIsArchived(userID, archived);
        return notes.stream().map(noteMapper::toDTO).toList();
    }

    @Override
    public List<NoteDTO> listNotesByUserAuth(Boolean archived) {
        Long userId = userInfo.getAuthenticatedUser().getId();
        return listNotesByUserId(userId, archived);
    }


    @Override
    public NoteDTO getNote(Long noteID) {
        return noteMapper.toDTO(findNoteById(noteID));
    }

    @Override
    public NoteDTO createNote(NoteDTO noteDTO) {
        Long userId = userInfo.getAuthenticatedUser().getId();
        noteDTO.setUserId(userId);
        NoteEntity noteEntity = noteMapper.toEntity(noteDTO);
        return noteMapper.toDTO(noteRepository.save(noteEntity));
    }

    @Override
    public NoteDTO updateNote(Long noteID, NoteDTO noteDTO) {
        NoteEntity existingNote = findNoteById(noteID);

        updateFieldIfNotNull(noteDTO.getTitle(), existingNote::setTitle);
        updateFieldIfNotNull(noteDTO.getContent(), existingNote::setContent);

        existingNote.setUpdatedAt(LocalDateTime.now());

        return noteMapper.toDTO(noteRepository.save(existingNote));
    }

    @Override
    public NoteDTO deleteNote(Long noteID) {
        NoteEntity note = findNoteById(noteID);
        noteRepository.delete(note);
        return noteMapper.toDTO(note);
    }

    @Override
    public NoteDTO archiveNote(Long noteID) {
        NoteEntity note = findNoteById(noteID);
        note.setArchived(!note.isArchived());
        return noteMapper.toDTO(noteRepository.save(note));
    }

    @Override
    public NoteWithTagsDTO saveTagsOnNote(Long noteID, List<Long> tagsIDs) {
        NoteEntity note = noteRepository.findById(noteID)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ErrorMessages.NOTE_NOT_FOUND, noteID)));

        List<TagEntity> tags = tagRepository.findAllById(tagsIDs);
        if (tags.size() != tagsIDs.size()) {
            throw new IllegalArgumentException(ErrorMessages.TAGS_NOT_FOUND);
        }

        note.getTags().addAll(tags);
        NoteEntity updatedNote = noteRepository.save(note);

        return noteWithTagsMapper.toDTO(updatedNote);
    }

    @Override
    public NoteWithTagsDTO saveTagOnNote(Long noteID, Long tagID) {
        NoteEntity note = noteRepository.findById(noteID)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ErrorMessages.NOTE_NOT_FOUND, noteID)
                ));

        TagEntity tag = tagRepository.findById(tagID)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ErrorMessages.TAG_NOT_EXIST, tagID)
                ));

        note.getTags().add(tag);
        NoteEntity updatedNote = noteRepository.save(note);

        return noteWithTagsMapper.toDTO(updatedNote);
    }

    private NoteEntity findNoteById(Long noteID) {
        return noteRepository.findById(noteID).orElseThrow(() ->
                new EntityNotFoundException(String.format(ErrorMessages.NOTE_NOT_FOUND, noteID))
        );
    }

    private <T> void updateFieldIfNotNull(T newValue, Consumer<T> setter) {
        if (newValue != null) {
            setter.accept(newValue);
        }
    }
}
