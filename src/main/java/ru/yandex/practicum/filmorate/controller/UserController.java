package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.EmailAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.IdGenerator;
import ru.yandex.practicum.filmorate.utils.Util;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final IdGenerator idGenerator;
    private final Set<String> emails = new HashSet<>();
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll() {
        log.info("Количество пользователей: {} ", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (emails.contains(user.getEmail())) {
            log.warn("Пользователь с email {} уже существует", user.getEmail());
            throw new EmailAlreadyExistException("Пользователь с email " + user.getEmail() + " уже существует");
        }
        Util.setUserNameIfEmpty(user);
        user.setId(idGenerator.generateId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        log.info("Пользователь с email {} успешно добавлен ", user.getEmail());
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            Util.setUserNameIfEmpty(user);
            if (users.get(user.getId()).getEmail().equals(user.getEmail())) {
                users.put(user.getId(), user);
                log.info("Данный пользователя успешно обновлены email {} ", user.getEmail());
                return user;
            } else if (emails.contains(user.getEmail())) {
                log.warn("Пользователь с email {} уже существует",user.getEmail());
                throw new EmailAlreadyExistException("Пользователь с email " + user.getEmail() + " уже существует");
            } else {
                emails.remove(users.get(user.getId()).getEmail());
                users.put(user.getId(), user);
                emails.add(user.getEmail());
                log.info("Данный пользователя успешно обновлены email {}", user.getEmail());
                return user;
            }
        } else {
            log.warn("Такого пользователя не существует");
            throw new ElementDoesNotExistException("Такого пользователя не существует");
        }

    }
}

