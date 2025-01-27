package com.app.dharbor.inotes.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NoteDTO {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private boolean isArchived;
}
