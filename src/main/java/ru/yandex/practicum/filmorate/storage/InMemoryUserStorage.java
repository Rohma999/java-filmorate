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
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private final IdGenerator idGenerator;

    @Autowired
    public InMemoryUserStorage(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public void create(User user) {
        Validator.setUserNameIfEmpty(user);
        user.setId(idGenerator.generateId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        log.info("Пользователь с email {} успешно добавлен ", user.getEmail());
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
    public void put(User user) {
        Validator.setUserNameIfEmpty(user);
        users.put(user.getId(), user);
        log.info("Данный пользователя успешно обновлены email {}", user.getEmail());
    }

    public Set<String> getEmails() {
        return emails;
    }
}
