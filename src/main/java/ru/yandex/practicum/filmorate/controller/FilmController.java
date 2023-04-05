package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Запрос на получение всех фильмов");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable long id) {
        log.info("Запрос на получение фильма с id {}", id);
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Запрос на создание фильма {}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        log.info("Запрос на обновление фильма {}", film);
        return filmService.put(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Запрос на добавление лайка фильму с id {} пользователем с id {}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Запрос на удаление лайка фильму с id {} пользователем с id {}", id, userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        log.info("Запрос на получение  топа из {} фильмов", count);
        return filmService.getPopularFilms(count);
    }
}

