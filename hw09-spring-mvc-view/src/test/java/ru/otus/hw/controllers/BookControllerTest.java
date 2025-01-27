package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
@DisplayName("Интеграционный тест контроллера книг")
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private GenreService genreService;

    @Autowired
    private ObjectMapper mapper;


    @Test
    @DisplayName("должен рендерить страницу книг с атрибутами модели и правильным названием view")
    void shouldRenderBooksPageWithCorrectViewAndModelAttributes() throws Exception {
        when(bookService.findAll()).thenReturn(getDbBooks());
        List<BookDto> expectedBooks = getDbBooks().stream()
                .map(BookDto::fromBook).toList();
        mockMvc.perform(get("/"))
                .andExpect(view().name("books"))
                .andExpect(model().attribute("books", expectedBooks));
    }

    @Test
    @DisplayName("должен рендерить страницу редактирования книги и содержать необходимые атрибуты модели")
    void shouldRenderEditBookPageWithCorrectViewAndModelAttributes() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(getDbBooks().get(0)));
        BookDto expectedBookDto = BookDto.fromBook(getDbBooks().get(0));
        List<AuthorDto> expectedAuthors = getDbAuthors().stream()
                .map(AuthorDto::fromAuthor).toList();
        List<GenreDto> expectedGenres = getDbGenres().stream()
                .map(GenreDto::fromGenre).toList();
        when(authorService.findAll()).thenReturn(getDbAuthors());
        when(genreService.findAll()).thenReturn(getDbGenres());
        mockMvc.perform(get("/edit-book").param("id", "1"))
                .andExpect(view().name("editBook"))
                .andExpect(model().attribute("book", expectedBookDto))
                .andExpect(model().attribute("authors", expectedAuthors))
                .andExpect(model().attribute("genres", expectedGenres));
    }

    @Test
    @DisplayName("должен сохранять отредактированную книгу и переходить на список всех книг")
    void shouldUpdateBookAndRedirectToContextPath() throws Exception {
        mockMvc.perform(
                post("/edit-book")
                 .param("id", "1")
                 .param("title", "Book_123")
                .param("authorDto.id", "1")
                .param("genreDto.id", "1")
                )
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).update(any(Book.class));
    }

    @Test
    @DisplayName("должен сохранять новую книгу и переходить на список всех книг")
    void shouldSaveBookAndRedirectToContextPath() throws Exception {
        mockMvc.perform(
                        post("/add-book")
                                .param("title", "Book_23")
                                .param("authorDto.id", "2")
                                .param("genreDto.id", "3")
                )
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).update(any(Book.class));
    }

    @Test
    @DisplayName("должен рендерить страницу создания книги и содержать необходимые атрибуты модели")
    void shouldRenderAddBookPageWithCorrectViewAndModelAttributes() throws Exception {
        BookDto expectedBookDto = new BookDto(null, null, new AuthorDto(null, null), new GenreDto(null, null));
        List<AuthorDto> expectedAuthors = getDbAuthors().stream()
                .map(AuthorDto::fromAuthor).toList();
        List<GenreDto> expectedGenres = getDbGenres().stream()
                .map(GenreDto::fromGenre).toList();
        when(authorService.findAll()).thenReturn(getDbAuthors());
        when(genreService.findAll()).thenReturn(getDbGenres());
        mockMvc.perform(get("/add-book"))
                .andExpect(view().name("addBook"))
                .andExpect(model().attribute("book", expectedBookDto))
                .andExpect(model().attribute("authors", expectedAuthors))
                .andExpect(model().attribute("genres", expectedGenres));
    }

    @Test
    @DisplayName("должен рендерить страницу с информацией об успешном удалении книги")
    void shouldRenderDeleteBookPageAndDeleteBook() throws Exception {
        mockMvc.perform(get("/delete-book").param("id", "1"))
                .andExpect(view().name("deleteBook"));
        verify(bookService, times(1)).deleteById(any(Long.class));
    }

    @Test
    @DisplayName("должен рендерить страницу ошибки если книга не найдена")
    void shouldRenderErrorPageWhenBookNotFound() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/edit-book").param("id", "1"))
                .andExpect(view().name("error"));
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(Long.valueOf(id), "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(Long.valueOf(id), "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(Long.valueOf(id), "Book_" + id, dbAuthors.get(id - 1), dbGenres.get(id - 1)))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}
