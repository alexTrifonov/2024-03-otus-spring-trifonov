package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;


import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    @Override
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findByBookId(String id) {
        return commentRepository.findByBookId(id);
    }


    @Transactional
    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment insert(String text, String bookId) {
        return save(null, text, bookId);
    }

    @Transactional
    @Override
    public Comment update(String id, String title, String bookId) {
        return save(id, title, bookId);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    private Comment save(String id, String text, String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        var comment = new Comment(id, text, book);
        return commentRepository.save(comment);
    }
}
