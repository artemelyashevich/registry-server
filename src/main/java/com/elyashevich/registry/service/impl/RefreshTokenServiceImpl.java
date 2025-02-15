package com.elyashevich.registry.service.impl;

import com.elyashevich.registry.entity.RefreshToken;
import com.elyashevich.registry.entity.User;
import com.elyashevich.registry.repository.RefreshTokenRepository;
import com.elyashevich.registry.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public String create(final String token, final User user) {
        log.debug("Attempting to create refresh token");

        var candidate = RefreshToken.builder()
                .token(token)
                .user(user)
                .build();
        this.refreshTokenRepository.save(candidate);

        log.info("Created refresh token");
        return token;
    }
}
