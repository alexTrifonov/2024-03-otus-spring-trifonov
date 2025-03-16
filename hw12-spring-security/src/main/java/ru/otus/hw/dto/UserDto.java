package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import ru.otus.hw.models.User;

public record UserDto(
        Long id,

        @NotBlank
        String login,

        @NotBlank
        String password
) {}
