package com.elyashevich.registry.api.controller;

import com.elyashevich.registry.api.dto.user.UserDto;
import com.elyashevich.registry.api.mapper.UserMapper;
import com.elyashevich.registry.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        var users = userService.findAll();
        var dtos = userMapper.toDto(users);

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(final @PathVariable("id") UUID id) {
        var user = this.userService.findById(id);
        var dto = this.userMapper.toDto(user);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> findByCurrentUser(final @RequestAttribute("email") String email) {
        var user = this.userService.findByEmail(email);
        var dto = this.userMapper.toDto(user);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(final @PathVariable("id") UUID id, final @Valid @RequestBody UserDto userDto) {
        var candidate = this.userMapper.toEntity(userDto);
        var user = this.userService.update(id, candidate);
        var dto = this.userMapper.toDto(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(final @PathVariable("id") UUID id) {
        this.userService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
