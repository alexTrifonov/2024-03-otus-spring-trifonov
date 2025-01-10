package ru.otus.hw.repositories;


import jakarta.annotation.Nonnull;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends CrudRepository<Genre, String> {
    List<Genre> findAll();

    Optional<Genre> findById(String id);

    @Nonnull
    @Override
    <S extends Genre> List<S> saveAll(Iterable<S> genres);
}
