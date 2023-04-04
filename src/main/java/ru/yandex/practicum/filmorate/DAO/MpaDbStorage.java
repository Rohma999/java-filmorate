package ru.yandex.practicum.filmorate.DAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository("MpaDbStorage")
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpa(int id) {
        String sql = "SELECT * " +
                "FROM rating_MPA r " +
                "WHERE R.ID = ?;";
        List<Mpa> mpas = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs), id));
        if (mpas.isEmpty()) {
            throw new ElementDoesNotExistException(String.format("Рейтинга с id %s не существует", id));
        }
        return mpas.get(0);
    }

    @Override
    public List<Mpa> findAll() {
        String sql = "SELECT * " +
                "FROM rating_MPA r " +
                "ORDER BY r.id;";
        List<Mpa> mpas = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs)));
        return mpas;
    }

    private Mpa makeMpa(ResultSet rss) throws SQLException {
        Mpa mpa = new Mpa(rss.getInt("id"), rss.getString("name"));
        return mpa;
    }
}
