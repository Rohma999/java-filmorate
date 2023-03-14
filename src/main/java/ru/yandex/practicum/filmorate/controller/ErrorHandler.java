package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.EmailAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;


import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse handleElementDoesNotExistException(ElementDoesNotExistException e) {
        log.warn("{}", e.getMessage());
        return new ErrorResponse("error", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleValidationException(ValidationException e) {
        log.warn("{}", e.getMessage());
        return new ErrorResponse("error", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> fieldErrors = e.getFieldErrors().stream()
                .map(fieldError ->
                        fieldError.getField() + ":" + fieldError.getDefaultMessage()).collect(Collectors.toList());
        log.warn("{}", e.getMessage());
        return new ErrorResponse("error", fieldErrors.toString());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse handleEmailAlreadyExistException(EmailAlreadyExistException e) {
        log.warn("{}", e.getMessage());
        return new ErrorResponse("error", e.getMessage());
    }
}
