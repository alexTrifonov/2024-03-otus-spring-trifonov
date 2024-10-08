package ru.otus.hw.repositories;


import org.springframework.data.repository.Repository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends Repository<Genre, Long> {
    List<Genre> findAll();

    Optional<Genre> findById(long id);
}
