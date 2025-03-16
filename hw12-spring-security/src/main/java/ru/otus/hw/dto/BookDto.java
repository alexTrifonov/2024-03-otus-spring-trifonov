package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.otus.hw.models.Book;


public record BookDto (
        Long id,

        @NotBlank(message = "Title field should not be blank")
        String title,

        @NotNull(message = "AuthorDto field should not be null")
        AuthorDto authorDto,

        @NotNull(message = "GenreDto field should not be null")
        GenreDto genreDto
) {

    public static BookDto fromBook(Book book) {
        return new BookDto(book.getId(), book.getTitle(), AuthorDto.fromAuthor(book.getAuthor()),
                GenreDto.fromGenre(book.getGenre()));
    }

    public Book toBook() {
        return new Book(id, title, authorDto.toAuthor(), genreDto.toGenre());
    }
}
