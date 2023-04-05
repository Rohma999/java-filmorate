package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Slf4j
@Service
public class GenreService {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(@Qualifier("GenreDbStorage") GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
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
