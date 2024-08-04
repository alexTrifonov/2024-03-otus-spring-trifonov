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
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами")
@DataJpaTest
@Import(JpaBookRepository.class)
public class JpaBookRepositoryTest {
    @Autowired
    private JpaBookRepository jpaBookRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getIdsBooks")
    void shouldReturnCorrectBookById(Integer id) {
        val actualBook = jpaBookRepository.findById(id);
        val expectedBook = testEntityManager.find(Book.class, id);
        assertThat(actualBook).isPresent()
                .get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        val actualBooks = jpaBookRepository.findAll();
        val expectedBooks = actualBooks.stream()
                .map(book -> testEntityManager.find(Book.class, book.getId()))
                .toList();
        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        val author = testEntityManager.find(Author.class, 1L);
        val genre = testEntityManager.find(Genre.class, 1L);
        var aBook = new Book(0, "BookTitle_10500", author, genre);
        var returnedBook = jpaBookRepository.save(aBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0);

        val actualBook = testEntityManager.find(Book.class, returnedBook.getId());
        assertThat(returnedBook)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(actualBook);


    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        val author = testEntityManager.find(Author.class, 1L);
        val genre = testEntityManager.find(Genre.class, 1L);
        var expectedBook = new Book(1L, "BookTitle_10500", author, genre);

        val bookByEm = testEntityManager.find(Book.class, expectedBook.getId());
        assertThat(bookByEm).isNotNull().isNotEqualTo(expectedBook);

        var returnedBook = jpaBookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(testEntityManager.find(Book.class, returnedBook.getId())).isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(testEntityManager.find(Book.class, 1L)).isNotNull();
        jpaBookRepository.deleteById(1L);
        testEntityManager.clear();
        assertThat(testEntityManager.find(Book.class, 1L)).isNull();
    }


    private static List<Integer> getIdsBooks() {
        return IntStream.range(1, 4).boxed().toList();
    }
}
