package com.app.dharbor.inotes.repository.data;

import com.app.dharbor.inotes.domain.entity.NoteEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoteRepository extends CrudRepository<NoteEntity, Long> {
    List<NoteEntity> findAllNotesByUserId(Long userId);
    List<NoteEntity> findAllNotesByUserIdAndIsArchived(Long userId, Boolean archived);
}
