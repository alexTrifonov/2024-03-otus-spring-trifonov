package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.otus.hw.models.Comment;

public record CommentDto(
        @NotNull
        Long id,

        @NotBlank
        String text,

        @NotNull
        BookDto bookDto
) {
    public static CommentDto fromComment(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), BookDto.fromBook(comment.getBook())
        );
    }

    public Comment toComment() {
        return new Comment(id, text, bookDto.toBook());
    }
}
