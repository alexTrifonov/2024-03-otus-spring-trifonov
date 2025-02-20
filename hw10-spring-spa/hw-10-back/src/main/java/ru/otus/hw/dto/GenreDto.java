package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import ru.otus.hw.models.Genre;

public record GenreDto(
        Long id,

        @NotBlank
        String name
) {
    public static GenreDto fromGenre(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    public Genre toGenre() {
        return new Genre(id, name);
    }
}
