package com.task.bookmark.service;

import com.task.bookmark.dto.UserDTO;
import com.task.bookmark.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    public UserDTO toUserDTO(User user) {
        return new UserDTO(user);
    }

    public List<UserDTO> toUserDTOList(List<User> userList) {
        return userList.stream().map(user -> new UserDTO(user)).collect(Collectors.toList());
    }

    public User toUser(UserDTO userDTO) {
        return new User(userDTO.getUsername(), userDTO.getPassword());
    }

}
