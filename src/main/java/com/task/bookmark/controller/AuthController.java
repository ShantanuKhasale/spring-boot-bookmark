package com.task.bookmark.controller;

import com.task.bookmark.dto.AuthenticationRequestDTO;
import com.task.bookmark.dto.AuthenticationResponseDTO;
import com.task.bookmark.exception.UniqueConstraintException;
import com.task.bookmark.payload.AuthenticationRequest;
import com.task.bookmark.payload.AuthenticationResponse;
import com.task.bookmark.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponseDTO> register(@Valid @RequestBody AuthenticationRequestDTO requestDTO) throws UniqueConstraintException {
        AuthenticationRequest authRequest = new AuthenticationRequest(requestDTO); // Convert To AuthRequest
        AuthenticationResponse request = authenticationService.register(authRequest); // Send To Service Layer
        AuthenticationResponseDTO response = new AuthenticationResponseDTO(request); // Convert back to AuthRequestDTO
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@Valid @RequestBody AuthenticationRequestDTO requestDTO) {
        AuthenticationRequest authRequest = new AuthenticationRequest(requestDTO); // Convert To AuthRequest
        AuthenticationResponse request = authenticationService.authenticate(authRequest); // Send To Service Layer
        AuthenticationResponseDTO response = new AuthenticationResponseDTO(request); // Convert back to AuthRequestDTO
        return ResponseEntity.ok(response);
    }
}
