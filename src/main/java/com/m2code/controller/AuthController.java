package com.m2code.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.m2code.dtos.LoginRequest;
import com.m2code.dtos.LoginResponse;
import com.m2code.dtos.UserDto;
import com.m2code.entities.User;
import com.m2code.exception.ResourceNotFoundException;
import com.m2code.repositories.UserRepository;
import com.m2code.services.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @PostMapping("/login")
    @Operation(summary = "login api")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws JsonProcessingException {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String token = jwtService.generateToken(user);
        LoginResponse response = LoginResponse.builder().token(token).user(modelMapper.map(user, UserDto.class)).build();
        return ResponseEntity.ok(response);
    }
}
