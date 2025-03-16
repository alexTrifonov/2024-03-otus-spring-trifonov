package ru.otus.hw.repositories;

import org.springframework.data.repository.Repository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends Repository<Author, Long> {
    List<Author> findAll();

    Optional<Author> findById(long id);
}
