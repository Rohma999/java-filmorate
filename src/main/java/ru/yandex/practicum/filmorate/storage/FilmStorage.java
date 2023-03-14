package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    void create(Film film);

    Collection<Film> findAll();

    Optional<Film> findFilm(Long id);

    void put(Film film);
}