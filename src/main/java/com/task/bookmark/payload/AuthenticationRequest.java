package com.task.bookmark.payload;

import com.task.bookmark.dto.AuthenticationRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    private String username;

    private String password;

    public AuthenticationRequest(AuthenticationRequestDTO request) {
        this.username = request.getUsername();
        this.password = request.getPassword();
    }
}
