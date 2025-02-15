package com.elyashevich.registry.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionBody {

    private final String message;
    private final Map<String, String> errors;

    public ExceptionBody(String message) {
        this.message = message;
        this.errors = new HashMap<>();
    }
}