package ru.otus.hw.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями")
@DataMongoTest
@ComponentScan("ru.otus.hw.services")
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("должен загружать список всех комментариев по id книги")
    @ParameterizedTest
    @MethodSource("getIdsBooks")
    void shouldReturnCorrectCommentsList(String id) {
        val comments = commentRepository.findByBookId(id);
        System.out.println(comments);
        if (id.equals("1")) {
            assertThat(comments.size()).isEqualTo(3);
        } else {
            assertThat(comments.size()).isEqualTo(1);
        }
    }

    private static List<String> getIdsBooks() {
        return IntStream.range(1, 4).boxed().map(Object::toString).toList();
    }
}
