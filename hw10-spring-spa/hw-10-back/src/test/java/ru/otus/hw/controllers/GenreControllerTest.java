package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(GenreController.class)
@DisplayName("Интеграционный тест контроллера жанров")
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private GenreService genreService;

    @Test
    @DisplayName("должен возвращать список DTO жанров")
    void shouldReturnCorrectGenreList() throws Exception {
        List<Genre> genres = List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2"));
        given(genreService.findAll()).willReturn(genres);

        List<GenreDto> expectedResult = genres.stream()
                .map(GenreDto::fromGenre).toList();

        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    @DisplayName("должен перехватывать ошибку и возвращать 404, если список жанров пуст")
    void shouldInterceptExceptionAndReturn404WhenGenresNotFound() throws Exception {
        given(genreService.findAll()).willReturn(List.of());
        assertDoesNotThrow(() -> mockMvc.perform(get("/api/genres")));
        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isNotFound());
    }
}
