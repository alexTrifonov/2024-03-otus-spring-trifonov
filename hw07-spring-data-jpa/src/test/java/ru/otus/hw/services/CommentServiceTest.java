package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DisplayName("Интеграционный тест сервиса комментариев")
@Import({BookServiceImpl.class, CommentServiceImpl.class})
public class CommentServiceTest {
    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private BookServiceImpl bookService;

    @Test
    @DisplayName("должен находить комментарий по id")
    public void shouldFindCommentById() {
        Book book = bookService.findById(1L).get();
        Optional<Comment> optionalComment = commentService.findById(1L);
        assertThat(optionalComment).isPresent();

        Comment comment = optionalComment.get();
        comment.setBook(book);
        Comment expectedComment = new Comment(1L, "test_comment_1", book);
        assertThat(comment).usingRecursiveComparison().isEqualTo(expectedComment);
    }


    @ParameterizedTest
    @DisplayName("должен находить все комментарии по id книги")
    @MethodSource("getIdsBooks")
    public void shouldFindAllCommentsByBookId(Integer bookId) {
        List<Comment> comments = commentService.findByBookId(bookId);
        if (bookId == 1) {
            assertThat(comments).hasSize(3);
        } else {
            assertThat(comments).hasSize(1);
        }
    }


    @Test
    @DisplayName("должен добавлять комментарий")
    public void shouldInsertComment() {
        Comment savedComment = commentService.insert("test_comment_1", bookService.findById(1L).get().getId());
        Comment foundComment = commentService.findById(savedComment.getId()).get();
        assertThat(foundComment).isEqualTo(savedComment);


        //восстановление исходного состояния БД для других тестов. я не нашел как это сделать лучше
        //если знаете решение лучше - подскажите конкретно как сделать
        commentService.deleteById(foundComment.getId());
    }

    @Test
    @DisplayName("должен обновлять комментарий")
    public void shouldUpdateComment() {
        Book book = bookService.findById(1L).get();
        Comment expectedComment = new Comment(1L, "test_comment_2", book);
        Comment persistComment = commentService.findById(1L).get();
        persistComment.setBook(book);
        assertThat(persistComment.getId()).isEqualTo(expectedComment.getId());
        assertThat(persistComment).isNotEqualTo(expectedComment);


        Comment updatedComment = commentService.update(expectedComment.getId(), expectedComment.getText(), expectedComment.getBook().getId());
        assertThat(updatedComment.getText()).isEqualTo(expectedComment.getText());

        Comment foundComment = commentService.findById(updatedComment.getId()).get();
        foundComment.setBook(book);
        assertThat(foundComment).usingRecursiveComparison().isEqualTo(expectedComment);




        //восстановление исходного состояния БД для других тестов. я не нашел как это сделать лучше
        //если знаете решение лучше - подскажите конкретно как сделать
        commentService.update(persistComment.getId(), persistComment.getText(), persistComment.getBook().getId());
    }

    @Test
    @DisplayName("должен удалять комментарий по id")
    public void shouldDeleteComment() {
        Book book = bookService.findById(2L).get();
        Comment newComment = commentService.insert("text", book.getId());
        Comment newFoundComment = commentService.findById(newComment.getId()).get();
        newFoundComment.setBook(book);
        assertThat(newFoundComment).usingRecursiveComparison().isEqualTo(newComment);

        commentService.deleteById(newComment.getId());

        Optional<Comment> foundComment = commentService.findById(newComment.getId());
        assertThat(foundComment).isEmpty();


    }

    private static List<Integer> getIdsBooks() {
        return IntStream.range(1, 4).boxed().toList();
    }

}