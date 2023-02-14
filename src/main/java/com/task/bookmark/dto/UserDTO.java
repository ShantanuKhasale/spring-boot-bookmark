package com.task.bookmark.dto;

import com.task.bookmark.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotBlank(message = "username must not be blank")
    @Size(min = 3, max = 16, message = "length should be between 3 to 16")
    private String username;
    @NotBlank(message = "password must not be blank")
    @Size(min = 6, message = "minimum password length is 6")
    private String password;

    private Long user_id;

    public UserDTO(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.user_id = user.getId();
    }

}
