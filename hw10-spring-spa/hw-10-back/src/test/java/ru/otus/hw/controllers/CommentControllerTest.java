package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.CommentService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@DisplayName("Интеграционный тест контроллера комментариев")
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentService commentService;

    @Test
    @DisplayName("должен возвращать корректный список комментариев")
    void shouldReturnCorrectCommentList() throws Exception {
        Author author = new Author(1L, "Author");
        Genre genre = new Genre(1L, "Genre");
        Book book = new Book(1L, "Book", author, genre);
        List<Comment> comments = List.of(new Comment(1L, "Text_1", book), new Comment(2L, "Text_2", book));
        given(commentService.findByBookId(1L)).willReturn(comments);

        List<CommentDto> commentDtos = comments.stream()
                        .map(CommentDto::fromComment)
                                .toList();

        mockMvc.perform(get("/api/comments?bookId=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentDtos)));
    }
}
