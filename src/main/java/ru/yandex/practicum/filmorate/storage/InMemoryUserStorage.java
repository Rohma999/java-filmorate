package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.IdGenerator;
import ru.yandex.practicum.filmorate.utils.Validator;

import java.util.*;

@Slf4j
@Component
@Deprecated
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private final IdGenerator idGenerator;

    @Autowired
    public InMemoryUserStorage(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public User create(User user) {
        Validator.setUserNameIfEmpty(user);
        user.setId(idGenerator.generateId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        log.info("Пользователь с email {} успешно добавлен ", user.getEmail());
        return user;
    }

    @Override
    public Collection<User> findAll() {
        Collection<User> userResponse = users.values();
        log.info("Количество пользователей: {} ", userResponse.size());
        return userResponse;
    }

    @Override
    public Optional<User> findUser(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User put(User user) {
        Validator.setUserNameIfEmpty(user);
        users.put(user.getId(), user);
        log.info("Данный пользователя успешно обновлены email {}", user.getEmail());
        return user;
    }

    public Set<String> getEmails() {
        return emails;
    }

    @Override
    public Collection<User> findCommonUserFriends(long id, long otherId) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }

    @Override
    public Collection<User> findUserFriends(long id) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }
}
