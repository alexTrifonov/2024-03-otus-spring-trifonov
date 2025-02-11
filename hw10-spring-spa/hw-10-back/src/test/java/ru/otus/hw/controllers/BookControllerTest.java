package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
@DisplayName("Интеграционный тест контроллера книг")
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbBooks = getDbBooks();
    }

    @Test
    @DisplayName("должен возвращать корректный список книг")
    void shouldReturnCorrectBookList() throws Exception {
        List<BookDto> expectedBookList = dbBooks.stream()
                .map(BookDto::fromBook)
                .toList();
        given(bookService.findAll()).willReturn(dbBooks);
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBookList)));

    }

    @Test
    @DisplayName("должен возвращать книгу по id")
    void shouldReturnCorrectBookById() throws Exception {
        BookDto expectedBookDto = BookDto.fromBook(dbBooks.get(0));
        given(bookService.findById(1L)).willReturn(Optional.of(dbBooks.get(0)));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBookDto)));
    }

    @Test
    @DisplayName("должен возвращать сохраненную книгу")
    void shouldReturnCorrectSavedBook() throws Exception {
        Book book = dbBooks.get(0);
        given(bookService.update(any())).willReturn(book);
        String expectedResult = objectMapper.writeValueAsString(BookDto.fromBook(book));

        mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DisplayName("должен возвращать обновленную книгу")
    void shouldReturnCorrectUpdatedBook() throws Exception {
        Book book = dbBooks.get(0);
        given(bookService.update(any())).willReturn(book);
        String expectedResult = objectMapper.writeValueAsString(BookDto.fromBook(book));

        mockMvc.perform(put("/api/books/1").contentType(MediaType.APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DisplayName("должен удалять книгу")
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());

    }

    private static List<Book> getDbBooks() {
        Author author = new Author(1L, "Author");
        Genre genre = new Genre(1L, "Genre");
        Book book = new Book(1L, "Book", author, genre);
        Author author2 = new Author(2L, "Author_2");
        Genre genre2 = new Genre(2L, "Genre_2");
        Book book2 = new Book(2L, "Book_2", author2, genre2);
        return List.of(book, book2);
    }
}
