package com.m2code.controller;

import com.m2code.dtos.LoginRequest;
import com.m2code.entities.User;
import com.m2code.exception.ResourceNotFoundException;
import com.m2code.repositories.UserRepository;
import com.m2code.services.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping("/login")
    @Operation(summary = "login api")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String token = jwtService.generateToken(user);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
}
