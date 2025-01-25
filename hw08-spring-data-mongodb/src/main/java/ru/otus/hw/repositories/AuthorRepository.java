package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends CrudRepository<Author, String> {
    @Override
    @Nonnull
    List<Author> findAll();

    @Nonnull
    Optional<Author> findById(String id);

    @Nonnull
    @Override
    <S extends Author> List<S> saveAll(Iterable<S> authors);
}
