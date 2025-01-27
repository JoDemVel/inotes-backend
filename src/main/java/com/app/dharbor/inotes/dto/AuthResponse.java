package com.app.dharbor.inotes.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonPropertyOrder({"username", "message", "jwt", "status"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String email;
    private String message;
    private String jwt;
    private Integer status;
}
