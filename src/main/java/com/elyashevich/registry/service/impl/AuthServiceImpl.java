package com.elyashevich.registry.service.impl;

import com.elyashevich.registry.api.dto.auth.JwtResponse;
import com.elyashevich.registry.api.dto.auth.ResetPasswordDto;
import com.elyashevich.registry.entity.Role;
import com.elyashevich.registry.entity.User;
import com.elyashevich.registry.exception.PasswordMismatchException;
import com.elyashevich.registry.service.AuthService;
import com.elyashevich.registry.service.RefreshTokenService;
import com.elyashevich.registry.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public JwtResponse login(final User user) {
        log.debug("Login attempt");

        var candidate = this.userService.loadUserByUsername(user.getEmail());
        if (!this.passwordEncoder.matches(user.getPassword(), candidate.getPassword())) {
            log.warn("Incorrect password");
            throw new PasswordMismatchException("Password mismatch");
        }
        // TODO: fix
        var response = this.authenticate(candidate, this.userService.findByEmail(user.getEmail()));

        log.info("User logged in successfully");
        return response;
    }

    @Transactional
    @Override
    public JwtResponse register(final User user) {
        log.debug("Register attempt");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole(Role.ROLE_USER);
        var candidate = this.userService.create(user);

        var response = this.authenticate(this.userService.loadUserByUsername(candidate.getEmail()), candidate);

        log.info("User registered successfully");
        return response;
    }

    @Override
    public JwtResponse refresh(final String token) {
        var email = TokenUtil.extractEmailClaims(token);
        var user = this.userService.loadUserByUsername(email);
        return new JwtResponse(TokenUtil.generateToken(user, 1000 * 60 * 30), token);
    }

    @Transactional
    @Override
    public void resetPassword(final ResetPasswordDto resetPasswordDto, final String email) {
        var oldUser = this.userService.findByEmail(email);
        if (!this.passwordEncoder.matches(oldUser.getPassword(), resetPasswordDto.oldPassword())) {
            log.warn("Incorrect password");
            throw new PasswordMismatchException("Password mismatch");
        }
        this.userService.resetPassword(oldUser.getId(), this.passwordEncoder.encode(resetPasswordDto.newPassword()));
    }

    private JwtResponse authenticate(final UserDetails user, final User candidate) {
        var accessToken = TokenUtil.generateToken(user, 1000 * 60 * 30);
        var refreshToken = TokenUtil.generateToken(user, 1000 * 60 * 60 * 24 * 10);

        this.refreshTokenService.create(refreshToken, candidate);
        return new JwtResponse(accessToken, refreshToken);
    }
}
