package com.elyashevich.registry.api.dto.user;

import java.sql.Date;
import java.util.UUID;

public record UserDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Date birthDate
) {
}
