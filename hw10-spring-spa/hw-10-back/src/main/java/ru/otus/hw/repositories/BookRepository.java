package ru.otus.hw.repositories;


import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {

    @EntityGraph(attributePaths = {"author", "genre"})
    Optional<Book> findById(long id);

    @Nonnull
    @Override
    @EntityGraph(attributePaths = {"author", "genre"})
    List<Book> findAll();
}
