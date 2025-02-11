package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/comments")
    public ResponseEntity<List<CommentDto>> listPage(@RequestParam("bookId") long bookId) {
        List<CommentDto> comments = commentService.findByBookId(bookId).stream()
                .map(CommentDto::fromComment).toList();
        return ResponseEntity.ok(comments);
    }
}
