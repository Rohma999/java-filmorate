package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {

    void create(User user);

    Collection<User> findAll();

    Optional<User> findUser(Long id);

    void put(User user);

    Set<String> getEmails();
}
