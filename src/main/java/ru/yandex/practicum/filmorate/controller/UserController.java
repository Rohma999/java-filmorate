package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.EmailAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.Util;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Set<String> emails = new HashSet<>();
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Количество пользователей: " + users.size());
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Некорректно заполнены поля пользователя :{}", bindingResult.getFieldErrors());
            throw new ValidationException("Некорректно заполнены поля :" + bindingResult.getFieldErrors());
        } else {
            if (emails.contains(user.getEmail())) {
                log.warn("Пользователь с email {} уже существует", user.getEmail());
                throw new EmailAlreadyExistException("Пользователь с email " + user.getEmail() + " уже существует");
            }
            if (user.getName() == null||user.getName().isEmpty() || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(Util.generateUserId());
            users.put(user.getId(), user);
            emails.add(user.getEmail());
            log.info("Пользователь с email {} успешно добавлен ", user.getEmail());
            return user;
        }
    }

    @PutMapping
    public User put(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Некорректно заполнены поля пользователя :{}", bindingResult.getFieldErrors());
            throw new ValidationException("Ошибка при обновлении данных пользователя: " + bindingResult.getFieldErrors());
        } else {
            if (users.containsKey(user.getId())) {
                if (Util.isEmailExist(user,users)) {
                    log.warn("Пользователь с email {} уже существует", user.getEmail());
                    throw new EmailAlreadyExistException("Пользователь с email " + user.getEmail() + " уже существует");
                }
                if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
                    user.setName(user.getLogin());
                }
                emails.remove(users.get(user.getId()).getEmail());
                users.put(user.getId(), user);
                emails.add(user.getEmail());
                log.info("Данный пользователя успешно обновлены email {} ", user.getEmail());
                return user;
            } else {
                log.warn("Такого пользователя не существует");
                throw new ElementDoesNotExistException("Такого пользователя не существует");
            }
        }
    }
}
