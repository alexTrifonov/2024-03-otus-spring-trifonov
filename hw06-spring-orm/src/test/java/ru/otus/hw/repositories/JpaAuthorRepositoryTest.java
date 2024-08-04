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

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами")
@DataJpaTest
@Import(JpaAuthorRepository.class)
public class JpaAuthorRepositoryTest {
    @Autowired
    private  JpaAuthorRepository authorRepository;

    @Autowired
    private  TestEntityManager testEntityManager;

    @DisplayName("должен загружать автора по id")
    @ParameterizedTest
    @MethodSource("getIdsAuthors")
    void shouldReturnCorrectAuthorById(Integer id) {
        val author = authorRepository.findById(id);
        val expectedAuthor = testEntityManager.find(Author.class, id);
        assertThat(author).isPresent()
                .get()
                .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectListOfAuthors() {
        val authors = authorRepository.findAll();
        assertThat(authors.size()).isEqualTo(3);
        authors.forEach(author -> {
            val expectedAuthor = testEntityManager.find(Author.class, author.getId());
            assertThat(author).usingRecursiveComparison().isEqualTo(expectedAuthor);
        });
    }

    private static List<Integer> getIdsAuthors() {
        return IntStream.range(1, 4).boxed().toList();
    }
}
