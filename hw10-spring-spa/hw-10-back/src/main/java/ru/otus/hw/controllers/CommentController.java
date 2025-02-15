package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/comments")
    public ResponseEntity<List<CommentDto>> listPage(@RequestParam("bookId") long bookId) {
        return ResponseEntity.ok(commentService.findByBookId(bookId).stream()
                .map(CommentDto::fromComment).toList());
    }
}
