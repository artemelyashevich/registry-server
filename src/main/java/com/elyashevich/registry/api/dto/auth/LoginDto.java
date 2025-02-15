package com.elyashevich.registry.api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record LoginDto(

        @NotNull(message = "Email must be not null")
        @NotEmpty(message = "Email must be not empty")
        @Email(message = "Invalid email format")
        String email,

        @NotNull(message = "Password must be not null")
        @NotEmpty(message = "Password must be not empty")
        @Length(min = 8, max = 255, message = "Password must be in {min} and {max}")
        String password
) {
}
