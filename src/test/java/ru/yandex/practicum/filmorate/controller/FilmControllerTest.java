package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.utils.IdGenerator;


import java.time.LocalDate;

public class FilmControllerTest {

    FilmController filmController ;

    @BeforeEach
    public void setUp() {
        filmController =
                new FilmController(new FilmService(new InMemoryFilmStorage(new IdGenerator()),
                        new InMemoryUserStorage(new IdGenerator())));
    }

    @Test
    public void shouldNotCreateFilmWithInvalidDate() {

        Film film = new Film("Зеленая миля", "Test description",
                LocalDate.of(100, 1, 1), 200);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    public void shouldNotUpdateFilmWithBadId() {

        Film film = new Film("Зеленая миля", "Test description",
                LocalDate.of(2012, 1, 1), 200);
        film.setId(1);

        assertThrows(ElementDoesNotExistException.class, () -> filmController.put(film));
    }
}