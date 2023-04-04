package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAO.MpaDbStorage;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.utils.Validator;

import java.util.*;


@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;

    private final GenreStorage genreStorage;

    private final LikeStorage likeStorage;

    private final MpaDbStorage mpaStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("GenreDbStorage") GenreStorage genreStorage,
                       @Qualifier("LikeDbStorage") LikeStorage likeStorage,
                       @Qualifier("MpaDbStorage") MpaDbStorage mpaStorage) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.likeStorage = likeStorage;
        this.mpaStorage = mpaStorage;
    }

    public Collection<Film> findAll() {
        Collection<Film> films = filmStorage.findAll();
        films.forEach(film -> film.getGenres().addAll(genreStorage.findGenreByFilmId(film.getId())));
        log.info("Передаем в контроллер все фильмы : {}", films);
        return films;
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.findFilm(id).orElseThrow(() ->
                new ElementDoesNotExistException("Фильма с id " + id + " не существет"));
        List<Genre> genres = genreStorage.findGenreByFilmId(film.getId());
        film.getGenres().addAll(genres);
        log.info("Передаем в контроллер фильм : {}", film);
        return film;
    }

    public Film create(Film film) {
        if (Validator.filmDateCheck(film)) {
            throw new ValidationException("Ошибка при добавлении фильма: дата релиза не может быть ранее " + Validator.DATE_LIMIT);
        }
        Film movie = filmStorage.create(film);
        if (movie.getGenres() != null) {
            genreStorage.batchGenreInsert(movie);
        }
        log.info("Передаем в контроллер созданный фильм : {}", film);
        return movie;
    }

    public Film put(Film film) {
        if (Validator.filmDateCheck(film)) {
            throw new ValidationException("Ошибка при добавлении фильма: дата релиза не может быть ранее " + Validator.DATE_LIMIT);
        }
        Film movie = filmStorage.put(film);
        if (film.getGenres() != null) {
            genreStorage.deleteFilmGenre(film.getId());
            genreStorage.batchGenreInsert(film);
        }
        log.info("Передаем в контроллер обновленный фильм : {}", film);
        return movie;
    }

    public void addLike(long id, long userId) {
        if (likeStorage.addLike(id, userId) == 0) {
            throw new ElementDoesNotExistException("Лайк уже поставлен");
        }
        log.info("Пользователь c id {} поставил лайк фильму c id {}", userId, id);
    }

    public void deleteLike(long id, long userId) {
        if (likeStorage.deleteLike(id, userId) == 0) {
            throw new ElementDoesNotExistException("Лайк от пользователя с id " + userId + " фильму с id " + id + " еще не поставлен");
        }
        log.info("Пользователь {} удалил лайк фильму {}", userId, id);
    }

    public Collection<Film> getPopularFilms(int count) {
        Collection<Film> films = filmStorage.getPopularFilms(count);
        log.info("Передаем в контроллер топ из {} фильмов : {}", count, films);
        return films;
    }

    public Collection<Mpa> getAllMpa() {
        Collection<Mpa> allMpa = mpaStorage.findAll();
        log.info("Передаем в контроллер все рейтинги : {}", allMpa);
        return allMpa;
    }

    public Mpa getMpa(int id) {
        Mpa mpa = mpaStorage.getMpa(id);
        log.info("Передаем в контроллер рейтинг с id {} : {}", id, mpa);
        return mpa;
    }

    public Collection<Genre> getGenres() {
        Collection<Genre> genres = genreStorage.findAll();
        log.info("Передаем в контроллер все жанры : {}", genres);
        return genres;
    }

    public Genre getGenre(int id) {
        Genre genre = genreStorage.getGenre(id);
        log.info("Передаем в контроллер жанр с id {} : {}", id, genre);
        return genre;
    }
}
