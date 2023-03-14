package ru.yandex.practicum.filmorate.exception;

public class ElementDoesNotExistException extends RuntimeException {
    public ElementDoesNotExistException(String message) {
        super((message));
    }
}
