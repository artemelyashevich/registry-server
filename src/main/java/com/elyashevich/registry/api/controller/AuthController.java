package com.elyashevich.registry.api.controller;

import com.elyashevich.registry.api.dto.auth.*;
import com.elyashevich.registry.api.mapper.UserMapper;
import com.elyashevich.registry.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(final @Valid @RequestBody LoginDto loginDto) {
        var user = this.userMapper.toEntity(loginDto);
        var response = this.authService.login(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(final @Valid @RequestBody RegisterDto registerDto) {
        var user = this.userMapper.toEntity(registerDto);
        var response = this.authService.register(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(final @Valid @RequestBody RefreshTokenDto dto) {
        var response = this.authService.refresh(dto.token());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/reset-password")
    @PreAuthorize("#email == authentication.principal")
    public ResponseEntity<Void> resetPassword(
            final @Valid @RequestBody ResetPasswordDto dto,
            final @RequestAttribute("email") String email
    ) {
        this.authService.resetPassword(dto, email);
        return ResponseEntity.accepted().build();
    }
}
