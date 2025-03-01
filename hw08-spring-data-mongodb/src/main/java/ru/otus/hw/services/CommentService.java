package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<Comment> findById(String id);

    List<Comment> findByBookId(String id);

    Comment save(Comment comment);

    Comment insert(String text, String bookId);

    Comment update(String id, String title, String bookId);

    void deleteById(String id);

    void delete(Comment comment);
}
