package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.Util;
import javax.validation.Valid;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    protected final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Количество фильмов: " + films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Некорректно заполнены поля фильма :{}", bindingResult.getFieldErrors());
            throw new ValidationException("Некорректно заполнены поля фильма :" + bindingResult.getFieldErrors());
        } else {
            if (film.getReleaseDate().isBefore(Util.DATE_LIMIT)) {
                log.warn("Ошибка при добавлении фильма: дата релиза не может быть ранее " + Util.DATE_LIMIT);
                throw new ValidationException("Ошибка при добавлении фильма: дата релиза не может быть ранее " + Util.DATE_LIMIT);
            }
            film.setId(Util.generateFilmId());
            films.put(film.getId(), film);
            log.info("Фильм «{}» успешно добавлен ", film.getName());
            return film;
        }
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Некорректно заполнены поля фильма :{}", bindingResult.getFieldErrors());
            throw new ValidationException("Ошибка при обновлении данных о фильме: " + bindingResult.getFieldErrors());
        } else {
            if (film.getReleaseDate().isBefore(Util.DATE_LIMIT)) {
                log.warn("Ошибка при добавлении фильма: дата релиза не может быть ранее " + Util.DATE_LIMIT);
                throw new ValidationException("Ошибка при добавлении фильма: дата релиза не может быть ранее " + Util.DATE_LIMIT);
            }
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                log.info("Данные фильма «{}» успешно обновлены", film.getName());
                return film;
            } else {
                log.warn("Фильм еще не добавлен");
                throw new ElementDoesNotExistException("Фильм еще не добавлен");
            }
        }
    }
}
