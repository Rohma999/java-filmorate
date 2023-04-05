package ru.yandex.practicum.filmorate.DAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;


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

    @Override
    public Collection<User> findCommonUserFriends(long id, long otherId) {
        String sql = "select * " +
                "from users as u where u.id IN" +
                "(SELECT f.FRIEND_ID " +
                "FROM FRIENDS f " +
                "WHERE f.USER_ID  IN (?, ?) " +
                "GROUP BY f.friend_id " +
                "HAVING count(friend_id) = 2);";
        List<User> commonFriends = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id, otherId);
        return commonFriends;
    }

    @Override
    public Collection<User> findUserFriends(long id) {
        String sql = "SELECT * FROM USERS u " +
                "WHERE u.ID IN(SELECT f.FRIEND_ID FROM FRIENDS f " +
                "WHERE f.user_id = ?);";
        List<User> friends = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
        return friends;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = User.builder().id(rs.getLong("id")).name(rs.getString("name"))
                .email(rs.getString("email"))
                .login(rs.getString("login")).birthday(rs.getDate("birthday").toLocalDate())
                .build();
        return user;
    }
}
