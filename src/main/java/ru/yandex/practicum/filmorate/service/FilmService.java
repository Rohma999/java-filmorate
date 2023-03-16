package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.Validator;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.filmStorage = inMemoryFilmStorage;
        this.userStorage = inMemoryUserStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getFilmById(long id) {
        return filmStorage.findFilm(id).orElseThrow(() -> new ElementDoesNotExistException("Такого фильма не существует"));
    }

    public Film create(Film film) {
        if (Validator.filmDateCheck(film)) {
            throw new ValidationException("Ошибка при добавлении фильма: дата релиза не может быть ранее " + Validator.DATE_LIMIT);
        }
        filmStorage.create(film);
        return film;
    }

    public Film put(Film film) {
        if (Validator.filmDateCheck(film)) {
            throw new ValidationException("Ошибка при добавлении фильма: дата релиза не может быть ранее " + Validator.DATE_LIMIT);
        }
        if (filmStorage.findFilm(film.getId()).isEmpty()) {
            throw new ElementDoesNotExistException("Фильм еще не добавлен");
        }
        filmStorage.put(film);
        return film;
    }

    public void addLike(long id, long userId) {
        Film film = filmStorage.findFilm(id).orElseThrow(() ->
                new ElementDoesNotExistException("Невозможно поставить лайк,фильм с id " + id + " не существует"));

        User user = userStorage.findUser(userId).orElseThrow(() ->
                new ElementDoesNotExistException("Невозможно поставить лайк,пользователя с id " + id + " не существует"));
        film.getLikes().add(userId);
        log.info("Пользователь {} поставил лайк фильму {}", user.getEmail(), film.getName());
    }

    public void deleteLike(long id, long userId) {
        Film film = filmStorage.findFilm(id).orElseThrow(() ->
                new ElementDoesNotExistException("Невозможно удалить лайк,фильм с id " + id + " не существует"));

        User user = userStorage.findUser(userId).orElseThrow(() ->
                new ElementDoesNotExistException("Невозможно удалить лайк,пользователя с id " + id + " не существует"));
        film.getLikes().remove(userId);
        log.info("Пользователь {} убрал лайк фильму {}", user.getEmail(), film.getName());
    }

    public Collection<Film> getPopularFilms(int count) {
        Collection<Film> films = filmStorage.findAll();
        Collection<Film> popularFilms = films.stream().sorted(Comparator.comparing(film -> -film.getLikes().size()))
                .limit(count).collect(Collectors.toList());
        log.info("Топ из {} фильмов : {}", count, popularFilms);
        return popularFilms;
    }
}
