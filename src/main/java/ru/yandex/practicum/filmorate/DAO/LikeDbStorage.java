package ru.yandex.practicum.filmorate.DAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Slf4j
@Repository("LikeDbStorage")
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addLike(long id, long userId) {
        String sql = "MERGE INTO LIKES AS tgt " +
                "USING VALUES (?,?) AS inc(FILM_ID,USER_ID) " +
                "ON (tgt.FILM_ID=inc.FILM_ID AND tgt.USER_ID=inc.USER_ID) " +
                "WHEN NOT MATCHED " +
                "THEN INSERT (FILM_ID, USER_ID) " +
                "VALUES (inc.FILM_ID, inc.USER_ID);";
        int count;
        try {
            count = jdbcTemplate.update(sql, id, userId);
        } catch (DataAccessException e) {
            throw new ElementDoesNotExistException("Невозможно добавить лайк,так как фильм или пользователь не существует");
        }
        return count;
    }

    @Override
    public int deleteLike(long id, long userId) {
        String sql = "DELETE FROM likes WHERE user_id = ? AND film_id = ?;";
        int count = jdbcTemplate.update(sql, userId, id);
        return count;
    }
}
