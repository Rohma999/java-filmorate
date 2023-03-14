package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class Util {

    public static final LocalDate DATE_LIMIT = LocalDate.of(1895, 12, 28);

    public static void setUserNameIfEmpty(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public static boolean filmDateCheck(Film film) {
        return film.getReleaseDate().isBefore(Util.DATE_LIMIT);
    }
}
