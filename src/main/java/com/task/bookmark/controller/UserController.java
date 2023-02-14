package com.task.bookmark.controller;


import com.task.bookmark.dto.UserDTO;
import com.task.bookmark.exception.ResourceNotFoundException;
import com.task.bookmark.exception.UniqueConstraintException;
import com.task.bookmark.model.User;
import com.task.bookmark.service.UserMapper;
import com.task.bookmark.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userMapper.toUserDTOList(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) throws UniqueConstraintException {
        userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        User userRequest = userMapper.toUser(userDTO); // Convert To User
        User user = userService.saveUser(userRequest); // Send To Service Layer
        UserDTO userResponse = userMapper.toUserDTO(user); // Convert back to UserDTO
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable(value = "userId") Long userId) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(userMapper.toUserDTO(userService.getUserById(userId)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "userId") Long userId) throws ResourceNotFoundException {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable(value = "userId") Long userId, @Valid @RequestBody UserDTO userDetails) throws ResourceNotFoundException, UniqueConstraintException {
        userDetails.setPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        User userRequest = userMapper.toUser(userDetails); // Convert to Model
        User user = userService.updateUser(userId, userRequest); // Send to Service Layer
        UserDTO userResponse = userMapper.toUserDTO(user); // Convert to DTO
        return ResponseEntity.ok(userResponse);
    }

}
