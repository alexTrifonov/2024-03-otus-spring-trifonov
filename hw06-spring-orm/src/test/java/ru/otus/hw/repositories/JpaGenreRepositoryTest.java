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
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами")
@DataJpaTest
@Import(JpaGenreRepository.class)
public class JpaGenreRepositoryTest {
    @Autowired
    private  JpaGenreRepository genreRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("должен загружать жанр по id")
    @ParameterizedTest
    @MethodSource("getIdsGenres")
    void shouldReturnCorrectGenreById(Integer id) {
        val genre = genreRepository.findById(id);
        val expectedGenre = testEntityManager.find(Genre.class, id);
        assertThat(genre).isPresent()
                .get()
                .usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectListOfGenres() {
        val genres = genreRepository.findAll();
        assertThat(genres.size()).isEqualTo(3);
        genres.forEach(genre -> {
            val expectedGenre = testEntityManager.find(Genre.class, genre.getId());
            assertThat(genre).usingRecursiveComparison().isEqualTo(expectedGenre);
        });
    }

    private static List<Integer> getIdsGenres() {
        return IntStream.range(1, 4).boxed().toList();
    }
}
