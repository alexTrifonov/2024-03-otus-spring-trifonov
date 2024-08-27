package ru.otus.hw.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами")
@DataJpaTest
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getIdsBooks")
    void shouldReturnCorrectBookById(Integer id) {
        val actualBook = bookRepository.findById(id);
        val expectedBook = testEntityManager.find(Book.class, id);
        assertThat(actualBook).isPresent()
                .get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        val actualBooks = bookRepository.findAll();
        val expectedBooks = actualBooks.stream()
                .map(book -> testEntityManager.find(Book.class, book.getId()))
                .toList();
        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
    }


    private static List<Integer> getIdsBooks() {
        return IntStream.range(1, 4).boxed().toList();
    }
}
