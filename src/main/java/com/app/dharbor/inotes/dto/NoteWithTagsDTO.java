package com.app.dharbor.inotes.dto;

import lombok.*;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NoteWithTagsDTO {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private boolean isArchived;
    private Set<String> tagNames;
}
