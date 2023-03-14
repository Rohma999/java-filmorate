package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.IdGenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private final IdGenerator idGenerator;

    @Autowired
    public InMemoryFilmStorage(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public void create(Film film) {
        film.setId(idGenerator.generateId());
        films.put(film.getId(), film);
        log.info("Фильм «{}» успешно добавлен ", film.getName());
    }

    @Override
    public Collection<Film> findAll() {
        log.info("Количество фильмов: {} ", films.size());
        return films.values();
    }

    @Override
    public Optional<Film> findFilm(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void put(Film film) {
        films.put(film.getId(), film);
        log.info("Данные фильма «{}» успешно обновлены", film.getName());
    }
}
