package com.app.dharbor.inotes.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TagDTO {
    private Long id;
    private String name;
    private Long userId;
}
