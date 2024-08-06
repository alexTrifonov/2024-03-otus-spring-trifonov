package ru.otus.hw.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями")
@DataJpaTest
@Import({JpaCommentRepository.class})
public class JpaCommentRepositoryTest {

    @Autowired
    private JpaCommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;


    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getIdsComments")
    void shouldReturnCorrectCommentById(Integer id) {
        val comment = commentRepository.findById(id);
        val expectedComment = testEntityManager.find(Comment.class, id);
        assertThat(comment).isPresent()
                .get()
                .usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список всех комментариев по id книги")
    @ParameterizedTest
    @MethodSource("getIdsBooks")
    void shouldReturnCorrectCommentsList(Integer id) {
        val comments = commentRepository.findByBookId(id);
        if (id == 1) {
            assertThat(comments.size()).isEqualTo(3);
        } else {
            assertThat(comments.size()).isEqualTo(1);
        }
        comments.forEach(actualComment -> {
            val expectedComment = testEntityManager.find(Comment.class, actualComment.getId());
            assertThat(actualComment).isEqualTo(expectedComment);
        });
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        val author = testEntityManager.find(Author.class, 1);
        val genre = testEntityManager.find(Genre.class, 1);
        var aBook = new Book(0, "BookTitle_10500", author, genre);
        Book persistedBook = testEntityManager.persist(aBook);
        var aComment = new Comment(0, "comment_text", persistedBook);
        var returnedComment = commentRepository.save(aComment);
        assertThat(returnedComment).isNotNull()
            .matches(comment -> comment.getId() > 0);

        val actualComment = testEntityManager.find(Comment.class, returnedComment.getId());

        assertThat(actualComment)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(actualComment);


    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var book = testEntityManager.find(Book.class, 1L);
        val expectedComment = new Comment(1L, "new_comment_text", book);
        var foundComment = testEntityManager.find(Comment.class, expectedComment.getId());
        assertThat(foundComment).isNotNull().isNotEqualTo(expectedComment);

        var updatedComment = commentRepository.save(expectedComment);
        assertThat(updatedComment).isNotNull()
                .usingRecursiveComparison().isEqualTo(expectedComment);

        assertThat(testEntityManager.find(Comment.class, updatedComment.getId())).isEqualTo(expectedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        assertThat(testEntityManager.find(Comment.class, 1L)).isNotNull();
        commentRepository.deleteById(1L);
        testEntityManager.flush();
        assertThat(testEntityManager.find(Comment.class, 1L)).isNull();
    }


    private static List<Integer> getIdsComments() {
        return IntStream.range(1, 6).boxed().toList();
    }

    private static List<Integer> getIdsBooks() {
        return IntStream.range(1, 4).boxed().toList();
    }
}
