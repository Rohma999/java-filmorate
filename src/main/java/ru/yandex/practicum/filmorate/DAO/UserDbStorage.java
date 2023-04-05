package ru.yandex.practicum.filmorate.DAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User create(User user) {
        long id = insertUser(user);
        user.setId(id);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        String sql = "select * from USERS";
        Collection<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
        return users;
    }

    @Override
    public Optional<User> findUser(Long id) {
        String sql = "select * from USERS where id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        if (!userRows.next()) {
            return Optional.empty();
        }
        return Optional.ofNullable(makeUser(userRows));
    }

    @Override
    public User put(User user) {
        String sql = "update USERS set NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? where id = ?";
        int count = jdbcTemplate.update(sql, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        if (count == 0) {
            throw new ElementDoesNotExistException("Пользователя с id " + user.getId() + " не существует");
        }
        return user;
    }

    private long insertUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        return simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();
    }

    private User makeUser(SqlRowSet srs) {
        User user = User.builder().id(srs.getLong("id")).name(srs.getString("name"))
                .email(srs.getString("email"))
                .login(srs.getString("login")).birthday(srs.getDate("birthday").toLocalDate())
        .build();
        return user;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = User.builder().id(rs.getLong("id")).name(rs.getString("name"))
                .email(rs.getString("email"))
        .login(rs.getString("login")).birthday(rs.getDate("birthday").toLocalDate())
                .build();
        return user;
    }
}
