package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;


import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        log.info("Запрос на получение всех пользователей");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable long id) {
        log.info("Запрос на получение пользователя с id {}", id);
        return userService.getUserById(id);
    }

    @GetMapping({"/{id}/friends"})
    public Collection<User> getFriends(@PathVariable long id) {
        log.info("Запрос на получение всех друзей пользователя с id {}", id);
        return userService.findUserFriends(id);
    }

    @GetMapping({"/{id}/friends/common/{otherId}"})
    public Collection<User> findCommonUserFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Запрос на получение общих друзей пользователя с id {} и пользователя с id {}", id, otherId);
        return userService.findCommonUserFriends(id, otherId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Запрос на создание пользователя : {}", user);
        return userService.create(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        log.info("Запрос на обновление пользователя : {}", user);
        return userService.put(user);
    }

    @PutMapping({"/{id}/friends/{friendId}"})
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Запрос на добавление в друзья пользователя с id {} к пользователя с id {}", friendId, id);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Запрос на удаление из друзей пользователя с id {} у пользователя с id {}", friendId, id);
        userService.deleteFriend(id, friendId);
    }
}

