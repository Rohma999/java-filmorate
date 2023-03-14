package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.EmailAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User findUserById(long id) {
        return userStorage.findUser(id).orElseThrow(() ->
                new ElementDoesNotExistException("Такого пользователя не существует"));
    }

    public User create(User user) {
        if (userStorage.getEmails().contains(user.getEmail())) {
            log.warn("Пользователь с email {} уже существует", user.getEmail());
            throw new EmailAlreadyExistException("Пользователь с email " + user.getEmail() + " уже существует");
        }
        userStorage.create(user);
        return user;
    }


    public User put(User user) {
        if (userStorage.findUser(user.getId()).isPresent()) {
            if (userStorage.findUser(user.getId()).get().getEmail().equals(user.getEmail())) {
                userStorage.put(user);
                return user;
            } else if (userStorage.getEmails().contains(user.getEmail())) {
                log.warn("Пользователь с email {} уже существует", user.getEmail());
                throw new EmailAlreadyExistException("Пользователь с email " + user.getEmail() + " уже существует");
            } else {
                userStorage.getEmails().remove(userStorage.findUser(user.getId()).get().getEmail());
                userStorage.put(user);
                userStorage.getEmails().add(user.getEmail());
                return user;
            }
        } else {
            log.warn("Такого пользователя не существует");
            throw new ElementDoesNotExistException("Такого пользователя не существует");
        }

    }

    public void addFriend(long id, long friendId) {
        User user = userStorage.findUser(id).orElseThrow(() ->
                new ElementDoesNotExistException("Невозможно добавить в друзья,пользователя с id " + id + " не существует"));

        User friend = userStorage.findUser(friendId).orElseThrow(() ->
                new ElementDoesNotExistException("Невозможно добавить в друзья,пользователя с id " + id + " не существует"));
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
        log.info("Пользователь {} добавил в друзья пользователя {}", id, friendId);
    }

    public void deleteFriend(long id, long friendId) {
        User user = userStorage.findUser(id).orElseThrow(() ->
                new ElementDoesNotExistException("Невозможно удалить из друзей,пользователя с id " + id + " не существует"));

        User friend = userStorage.findUser(friendId).orElseThrow(() ->
                new ElementDoesNotExistException("Невозможно удалить из друзей,,пользователя с id " + id + " не существует"));
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
        log.info("Пользователь {} удалил из друзей пользователя {}", id, friendId);
    }

    public Collection<User> findUserFriends(long id) {
        User user = userStorage.findUser(id).orElseThrow(() ->
                new ElementDoesNotExistException(("Такого пользователя не существует")));

        return user.getFriends().stream().map(userStorage::findUser)
                .map(Optional::get).collect(Collectors.toList());
    }

    public Collection<User> findCommonUserFriends(long id, long otherId) {
        User user = userStorage.findUser(id).orElseThrow(() ->
                new ElementDoesNotExistException(("Пользователя с id " + id + " не существует")));

        User otherUser = userStorage.findUser(otherId).orElseThrow(() ->
                new ElementDoesNotExistException(("Пользователя с id " + otherId + " не существует")));

        return user.getFriends().stream().filter(otherUser.getFriends()::contains).
                map(userId -> userStorage.findUser(userId).get()).collect(Collectors.toList());
    }

}
