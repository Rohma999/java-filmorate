package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    Genre getGenre(int id);

    List<Genre> findAll();

    List<Genre> findGenreByFilmId(Long id);

    void deleteFilmGenre(long id);

    void batchGenreInsert(Film film);

}
