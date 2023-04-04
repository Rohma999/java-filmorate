package ru.yandex.practicum.filmorate.DAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        long id = insertFilm(film);
        film.setId(id);
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        String sql = "select f.id as id, " +
                "f.name as name, " +
                "f.description as description, " +
                "f.release_date as release_date," +
                " f.duration as duration, " +
                "f.rating_id as rating_id," +
                " r.name as rating_name " +
                "from FILMS as f " +
                "left join RATING_MPA as r on f.rating_id = r.id ";
        List<Film> films = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs)));
        return films;
    }

    @Override
    public Optional<Film> findFilm(Long id) {
        String sql = "select f.id as id, " +
                "f.name as name, " +
                "f.description as description, " +
                "f.release_date as release_date," +
                " f.duration as duration, " +
                "f.rating_id as rating_id," +
                " r.name as rating_name " +
                "from FILMS as f " +
                "left join RATING_MPA as r on f.rating_id = r.id " +
                "where f.id = ?;";
        List<Film> film = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id));
        if (film.size() == 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(film.get(0));
    }

    @Override
    public Film put(Film film) {
        String sql = "update FILMS set NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_ID = ? where ID = ?";

        int count = jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        if (count == 0) {
            throw new ElementDoesNotExistException("Фильма c id " + film.getId() + " не существует");
        }
        return film;
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        String sql = "select f.id ,f.name as name, description, release_date, duration, f.rating_id as rating_id, r.NAME as rating_name, count(FILM_ID) " +
                "from FILMS as f " +
                "left join LIKES as l on f.ID = l.FILM_ID " +
                "left join RATING_MPA as r on f.RATING_ID = r.ID " +
                "group by f.id, r.ID " +
                "order by count(FILM_ID) desc " +
                "limit ? ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
    }

    private Film makeFilm(ResultSet rss) throws SQLException {
        Film film = Film.builder().id(rss.getInt("id"))
                .name(rss.getString("name"))
        .description(rss.getString("description"))
        .duration(rss.getInt("duration"))
        .releaseDate(rss.getDate("release_date").toLocalDate())
                .build();
        int id = rss.getInt("rating_id");
        String name = rss.getString("rating_name");
        film.setMpa(new Mpa(id, name));
        return film;
    }

    private long insertFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        return simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue();
    }
}
