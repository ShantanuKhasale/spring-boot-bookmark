package com.task.bookmark.dto;

import com.task.bookmark.payload.AuthenticationResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDTO {
    private String token;

    public AuthenticationResponseDTO(AuthenticationResponse request) {
        this.token = request.getToken();
    }
}