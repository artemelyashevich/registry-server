package com.elyashevich.registry.api.mapper;

import com.elyashevich.registry.api.dto.auth.LoginDto;
import com.elyashevich.registry.api.dto.auth.RegisterDto;
import com.elyashevich.registry.api.dto.user.UserDto;
import com.elyashevich.registry.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserDto toDto(final User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBirthDate()
        );
    }

    public List<UserDto> toDto(final List<User> users) {
        return users.stream()
                .map(this::toDto)
                .toList();
    }

    public User toEntity(final UserDto userDto) {
        return User.builder()
                .email(userDto.email())
                .firstName(userDto.firstName())
                .lastName(userDto.lastName())
                .birthDate(userDto.birthDate())
                .build();
    }

    public User toEntity(final LoginDto loginDto) {
        return User.builder()
                .email(loginDto.email())
                .password(loginDto.password())
                .build();
    }

    public User toEntity(final RegisterDto registerDto) {
        return User.builder()
                .firstName(registerDto.firstName())
                .lastName(registerDto.lastName())
                .email(registerDto.email())
                .birthDate(registerDto.birthDate())
                .password(registerDto.password())
                .build();
    }
}
