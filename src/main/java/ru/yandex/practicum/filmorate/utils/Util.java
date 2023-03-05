package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;

public class Util {

    public static final LocalDate DATE_LIMIT = LocalDate.of(1895, 12, 28);

    public static void userNameCheck(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
