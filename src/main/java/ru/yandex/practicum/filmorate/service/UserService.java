package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.Validator;

import java.util.Collection;


@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    private final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage,
                       @Qualifier("FriendDbStorage") FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User getUserById(long id) {
        return userStorage.findUser(id).orElseThrow(() ->
                new ElementDoesNotExistException("Такого пользователя не существует"));
    }

    public User create(User user) {
        Validator.setUserNameIfEmpty(user);
        User dbUser = userStorage.create(user);
        return dbUser;
    }

    public User put(User user) {
        Validator.setUserNameIfEmpty(user);
        User dbUser = userStorage.put(user);
        return dbUser;
    }

    public void addFriend(long id, long friendId) {
        if (id == friendId) {
            throw new ValidationException("Пользователь не может добавить в друзья самого себя");
        }
        if (friendStorage.addFriend(id, friendId) == 0) {
            throw new ElementDoesNotExistException("Пользователь уже добавлен в друзья");
        }
        log.info("Пользователь c id {} добавил в друзья пользователя c id {}", id, friendId);
    }

    public void deleteFriend(long id, long friendId) {
        if (friendStorage.deleteFriend(id, friendId) == 0) {
            throw new ElementDoesNotExistException("Пользователя с id +" + friendId + "нет в друзьях у пользователя с id " + id);
        }
        log.info("Пользователь c id {} удалил из друзей пользователя с id {}", id, friendId);
    }

    public Collection<User> findUserFriends(long id) {
        Collection<User> friends = userStorage.findUserFriends(id);
        log.info("Друзья пользователя с id {} : {}", id, friends);
        return friends;
    }

    public Collection<User> findCommonUserFriends(long id, long otherId) {
        Collection<User> commonFriends = userStorage.findCommonUserFriends(id, otherId);
        log.info("Общие друзья пользователей с id {} и {} : {}", id, otherId, commonFriends);
        return commonFriends;
    }
}
