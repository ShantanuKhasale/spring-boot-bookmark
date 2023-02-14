package com.task.bookmark.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDTO {

    @NotBlank(message = "username must not be blank")
    @Size(min = 3, max = 16, message = "length should be between 3 to 16")
    private String username;
    @NotBlank(message = "password must not be blank")
    @Size(min = 6, message = "minimum password length is 6")
    private String password;
}
