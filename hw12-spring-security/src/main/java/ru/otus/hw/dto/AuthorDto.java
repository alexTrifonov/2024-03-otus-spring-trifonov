package ru.otus.hw.dto;


import jakarta.validation.constraints.NotBlank;

import ru.otus.hw.models.Author;

public record AuthorDto(

    Long id,

    @NotBlank
    String fullName
) {
    public static AuthorDto fromAuthor(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }

    public Author toAuthor() {
        return new Author(id, fullName);
    }
}
