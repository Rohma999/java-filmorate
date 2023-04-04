package ru.yandex.practicum.filmorate.DAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.storage.FriendStorage;


@Slf4j
@Repository("FriendDbStorage")
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addFriend(long id, long userId) {
        String sql = "MERGE INTO FRIENDS AS tgt " +
                "USING VALUES (?,?) AS inc(USER_ID,FRIEND_ID) " +
                "ON (tgt.USER_ID=inc.USER_ID AND tgt.FRIEND_ID=inc.FRIEND_ID) " +
                "WHEN NOT MATCHED " +
                "THEN INSERT (USER_ID, FRIEND_ID) " +
                "VALUES (inc.USER_ID, inc.FRIEND_ID);";
        int count;
        try {
            count = jdbcTemplate.update(sql, id, userId);
        } catch (DataAccessException e) {
            throw new ElementDoesNotExistException("Невозможно добавить в друзья,так как одного из пользователей не существует");
        }
        return count;
    }


    @Override
    public int deleteFriend(long id, long userId) {
        String sql = "DELETE FROM FRIENDS WHERE user_id = ? AND FRIEND_ID = ?;";
        int count = jdbcTemplate.update(sql, id, userId);
        return count;
    }
}
