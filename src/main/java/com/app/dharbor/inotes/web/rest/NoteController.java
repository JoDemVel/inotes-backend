package com.app.dharbor.inotes.web.rest;

import com.app.dharbor.inotes.common.Path;
import com.app.dharbor.inotes.dto.NoteDTO;
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
        return new ResponseEntity<>(this.noteService.listNotes(archived), HttpStatus.OK);
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
    public ResponseEntity<NoteDTO> archiveNote(@PathVariable Long id){
        return new ResponseEntity<>(this.noteService.archiveNote(id), HttpStatus.OK);
    }
}
