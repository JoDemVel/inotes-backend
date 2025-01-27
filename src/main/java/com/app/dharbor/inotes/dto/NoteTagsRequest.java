package com.app.dharbor.inotes.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record NoteTagsRequest(List<Long> tags) {
}
