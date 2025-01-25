package ru.otus.hw.repositories;


import jakarta.annotation.Nonnull;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, String> {

    Optional<Book> findById(long id);

    @Nonnull
    @Override

    List<Book> findAll();
}
