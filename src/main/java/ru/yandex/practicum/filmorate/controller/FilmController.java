package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.info("Запрос на получение всех фильмов");
        return filmService.findAll();
    }

    @GetMapping("/films/{id}")
    public Film findFilm(@PathVariable long id) {
        log.info("Запрос на получение фильма с id {}", id);
        return filmService.getFilmById(id);
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("Запрос на создание фильма {}", film);
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film put(@Valid @RequestBody Film film) {
        log.info("Запрос на обновление фильма {}", film);
        return filmService.put(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Запрос на добавление лайка фильму с id {} пользователем с id {}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Запрос на удаление лайка фильму с id {} пользователем с id {}", id, userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public Collection<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        log.info("Запрос на получение  топа из {} фильмов", count);
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaById(@PathVariable int id) {
        log.info("Запрос на получение  рейтинга с id {}", id);
        return filmService.getMpa(id);
    }

    @GetMapping("/mpa")
    public Collection<Mpa> getMpas() {
        log.info("Запрос на получение всех рейтингов");
        return filmService.getAllMpa();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable int id) {
        log.info("Запрос на получение рейтинга с id {}", id);
        return filmService.getGenre(id);
    }

    @GetMapping("/genres")
    public Collection<Genre> getGenre() {
        log.info("Запрос на получение всех жанров");
        return filmService.getGenres();
    }
}

