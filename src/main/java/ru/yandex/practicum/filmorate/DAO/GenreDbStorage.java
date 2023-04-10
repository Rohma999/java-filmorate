package ru.yandex.practicum.filmorate.DAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository("GenreDbStorage")
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenre(int id) {
        String sql = "SELECT * " +
                "FROM GENRES g " +
                "WHERE G.ID = ?;";
        List<Genre> genres = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id));
        if (genres.isEmpty()) {
            throw new ElementDoesNotExistException(String.format("Жанра с id %s не существует", id));
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> findAll() {
        String sql = "SELECT * " +
                "FROM GENRES g " +
                "ORDER BY g.id;";
        List<Genre> genres = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs)));
        return genres;
    }

    @Override
    public List<Genre> findGenreByFilmId(Long id) {
        String sql = "SELECT * " +
                "FROM FILM_GENRE fg " +
                "JOIN GENRES g ON FG .GENRE_ID = g.ID " +
                "WHERE FG .FILM_ID = ? " +
                "ORDER BY g.id;";
        List<Genre> genres = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id));
        return genres;
    }

    @Override
    public void batchGenreInsert(Film film) {
        String sql = "INSERT INTO FILM_GENRE (film_id, genre_id) VALUES(?, ?);";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                List<Genre> genres = new ArrayList<>(film.getGenres());
                preparedStatement.setInt(1, (int) film.getId());
                preparedStatement.setInt(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return film.getGenres().size();
            }
        });
    }

    @Override
    public void deleteFilmGenre(long id) {
        String sql = "delete from FILM_GENRE where FILM_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    private Genre makeGenre(ResultSet rss) throws SQLException {
        Genre genre = new Genre(rss.getInt("id"), rss.getString("name"));
        return genre;
    }
}
