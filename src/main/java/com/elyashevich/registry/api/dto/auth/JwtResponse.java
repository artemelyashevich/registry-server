package com.elyashevich.registry.api.dto.auth;

public record JwtResponse(
        String accessToken,
        String refreshToken
) {
}
