package com.elyashevich.registry.service;

import com.elyashevich.registry.api.dto.auth.JwtResponse;
import com.elyashevich.registry.api.dto.auth.ResetPasswordDto;
import com.elyashevich.registry.entity.User;
import jakarta.validation.constraints.NotNull;

public interface AuthService {

    JwtResponse login(final User user);

    JwtResponse register(final User user);

    JwtResponse refresh(final String token);

    void resetPassword(final ResetPasswordDto resetPasswordDto, final String email);
}
