package ru.otus.hw.exceptions;

public class AuthorFillException extends RuntimeException {
    public AuthorFillException(String message, Throwable cause) {
        super(message, cause);
    }
}
