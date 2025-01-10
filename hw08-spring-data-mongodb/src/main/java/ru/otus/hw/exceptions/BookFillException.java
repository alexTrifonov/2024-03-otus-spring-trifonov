package ru.otus.hw.exceptions;

public class BookFillException extends RuntimeException {
    public BookFillException(String message, Throwable cause) {
        super(message, cause);
    }
}
