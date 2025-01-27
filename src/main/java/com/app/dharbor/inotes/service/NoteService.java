package com.app.dharbor.inotes.service;

import com.app.dharbor.inotes.dto.NoteDTO;

import java.util.List;

public interface NoteService {
    List<NoteDTO> listNotesByUserId(Long userID, Boolean archived);
    NoteDTO getNote(Long noteID);
    NoteDTO createNote(NoteDTO noteDTO);
    NoteDTO updateNote(Long noteID, NoteDTO noteDTO);
    NoteDTO deleteNote(Long noteID);
    NoteDTO archiveNote(Long noteID);
}
