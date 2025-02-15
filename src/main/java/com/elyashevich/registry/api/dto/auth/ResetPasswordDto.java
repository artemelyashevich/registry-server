package com.elyashevich.registry.api.dto.auth;

public record ResetPasswordDto(
        String oldPassword,
        String newPassword
) {
}
