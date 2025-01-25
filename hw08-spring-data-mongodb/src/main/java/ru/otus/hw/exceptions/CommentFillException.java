package ru.otus.hw.exceptions;

public class CommentFillException extends RuntimeException {
    public CommentFillException(String message, Throwable cause) {
        super(message, cause);
    }
}
