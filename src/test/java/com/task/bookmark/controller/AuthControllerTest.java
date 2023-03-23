package com.task.bookmark.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.bookmark.AbstractContainerBaseTest;
import com.task.bookmark.dto.AuthenticationResponseDTO;
import com.task.bookmark.dto.UserDTO;
import com.task.bookmark.model.Role;
import com.task.bookmark.model.User;
import com.task.bookmark.repository.UserRepository;
import com.task.bookmark.service.JwtService;
import com.task.bookmark.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest extends AbstractContainerBaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;


    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
    }


    @Test
    public void shouldSignupUser() throws Exception {
        // given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("testpassword");

        // when
        MvcResult result = mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andReturn();
        // then
        String responseBody = result.getResponse().getContentAsString();
        AuthenticationResponseDTO response = new ObjectMapper().readValue(responseBody, AuthenticationResponseDTO.class);
        Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        Assertions.assertEquals(userDTO.getUsername(), jwtService.extractUsername(response.getToken()));

    }

    @Test
    public void shouldLoginUser() throws Exception {
        // given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setPassword("password");
        User user = new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(Role.USER);
        userService.saveUser(user);

        // when
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andReturn();
        // then
        String responseBody = result.getResponse().getContentAsString();
        AuthenticationResponseDTO response = new ObjectMapper().readValue(responseBody, AuthenticationResponseDTO.class);
        Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        Assertions.assertEquals(userDTO.getUsername(), jwtService.extractUsername(response.getToken()));
    }

    @Test
    public void shouldReturnBadRequestForInvalidInput() throws Exception {
        // given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("demoUser");

        // when
        userDTO.setPassword("123"); // Minimum password length is 6
        MvcResult result = mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andReturn();
        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    public void shouldReturnUnauthorizedForInvalidPassword() throws Exception {
        // given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("username5");
        userDTO.setPassword("password");
        User user = new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(Role.USER);
        userService.saveUser(user);

        // when
        userDTO.setPassword("123456"); // Invalid Password
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andReturn();

        // then
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getResponse().getStatus());
    }

    @Test
    public void shouldReturnConflictForExistingUser() throws Exception {
        // given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test1");
        userDTO.setPassword("password");
        User user = new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(Role.USER);
        userService.saveUser(user);

        // when
        MvcResult result = mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andReturn();

        // then
        Assertions.assertEquals(HttpStatus.CONFLICT.value(), result.getResponse().getStatus());
    }

}
