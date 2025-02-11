package ru.otus.hw.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;
import ru.otus.hw.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/books")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.findAll().stream()
                .map(BookDto::fromBook).toList();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/api/books/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable("id") long id) {
        BookDto bookDto;
        Optional<Book> optionalBook = bookService.findById(id);
        if (optionalBook.isPresent()) {
            bookDto = BookDto.fromBook(optionalBook.get());
        } else {
            throw new EntityNotFoundException("Book not found");
        }
        return ResponseEntity.ok(bookDto);
    }

    @PostMapping("/api/books")
    public ResponseEntity<BookDto> addBook(@Valid @RequestBody BookDto bookDto) {
        Book savedBook = bookService.update(bookDto.toBook());
        return ResponseEntity.ok(BookDto.fromBook(savedBook));
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/books/{id}")
    public ResponseEntity<BookDto> updateBook(@Valid @RequestBody BookDto bookDto) {
        Book updatedBook = bookService.update(bookDto.toBook());
        return ResponseEntity.ok(BookDto.fromBook(updatedBook));
    }
}
