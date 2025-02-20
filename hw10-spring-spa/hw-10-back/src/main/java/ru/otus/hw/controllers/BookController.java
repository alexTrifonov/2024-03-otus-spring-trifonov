package ru.otus.hw.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.exceptions.EntityNotFoundException;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/books")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAll().stream()
                .map(BookDto::fromBook).toList());
    }

    @GetMapping("/api/books/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable("id") long id) {
        return bookService.findById(id)
                .map(BookDto::fromBook)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));
    }

    @PostMapping("/api/books")
    public ResponseEntity<BookDto> addBook(@Valid @RequestBody BookDto bookDto) {
        return ResponseEntity.ok(BookDto.fromBook(bookService.update(bookDto.toBook())));
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/books/{id}")
    public ResponseEntity<BookDto> updateBook(@Valid @RequestBody BookDto bookDto) {
        return ResponseEntity.ok(BookDto.fromBook(bookService.update(bookDto.toBook())));
    }
}
