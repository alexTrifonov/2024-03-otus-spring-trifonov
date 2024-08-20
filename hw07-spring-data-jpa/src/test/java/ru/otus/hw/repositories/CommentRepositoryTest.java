package ru.otus.hw.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями")
@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;


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

    private static List<Integer> getIdsBooks() {
        return IntStream.range(1, 4).boxed().toList();
    }
}
