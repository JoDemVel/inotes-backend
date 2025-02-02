package com.app.dharbor.inotes.web.rest;

import com.app.dharbor.inotes.common.Path;
import com.app.dharbor.inotes.dto.NoteDTO;
import com.app.dharbor.inotes.dto.NoteTagsRequest;
import com.app.dharbor.inotes.dto.NoteWithTagsDTO;
import com.app.dharbor.inotes.service.implement.NoteServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class NoteController {
    private final NoteServiceImpl noteService;

    public NoteController(NoteServiceImpl noteService) {
        this.noteService = noteService;
    }

    @GetMapping(Path.NOTE_ID)
    public ResponseEntity<NoteDTO> getNoteById(@PathVariable Long id) {
        return new ResponseEntity<>(this.noteService.getNote(id), HttpStatus.OK);
    }

    @GetMapping(Path.NOTE)
    public ResponseEntity<List<NoteDTO>> getAllNotes(@RequestParam(value = "archived", required = false) Boolean archived) {
        return new ResponseEntity<>(this.noteService.listNotesByUserAuth(archived), HttpStatus.OK);
    }

    @PostMapping(Path.NOTE)
    public ResponseEntity<NoteDTO> create(@RequestBody @Valid NoteDTO note){
        return new ResponseEntity<>(this.noteService.createNote(note), HttpStatus.CREATED);
    }

    @PutMapping(Path.NOTE_ID)
    public ResponseEntity<NoteDTO> update(@PathVariable Long id, @RequestBody @Valid NoteDTO note){
        return new ResponseEntity<>(this.noteService.updateNote(id, note), HttpStatus.OK);
    }

    @DeleteMapping(Path.NOTE_ID)
    public ResponseEntity<NoteDTO> delete(@PathVariable Long id) {
        return new ResponseEntity<>(this.noteService.deleteNote(id), HttpStatus.OK);
    }

    @PatchMapping(Path.NOTE_ID)
    public ResponseEntity<NoteDTO> toggleArchiveNote(@PathVariable Long id){
        return new ResponseEntity<>(this.noteService.archiveNote(id), HttpStatus.OK);
    }

    @PatchMapping(Path.NOTE_ID_TAGS)
    public ResponseEntity<NoteWithTagsDTO> saveTagsOnNote(
            @PathVariable Long id,
            @RequestBody @Valid NoteTagsRequest tags) {
        return new ResponseEntity<>(noteService.saveTagsOnNote(id, tags.tags()), HttpStatus.OK);
    }

    @PatchMapping(Path.NOTE_ID_TAGS_ID)
    public ResponseEntity<NoteWithTagsDTO> saveTagOnNote(
            @PathVariable Long id,
            @PathVariable Long tagId) {
        return new ResponseEntity<>(noteService.saveTagOnNote(id, tagId), HttpStatus.OK);
    }

    @GetMapping(Path.NOTE_SEARCH)
    public ResponseEntity<List<NoteDTO>> searchNotes(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String tagName) {
        return new ResponseEntity<>(noteService.searchNotes(title, content, tagName), HttpStatus.OK);
    }
}
