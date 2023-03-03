package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;

public class Util {
    static  int FilmId =0;
    static int UserId =0;
    public static final LocalDate DATE_LIMIT = LocalDate.of(1895, 12, 28);

    public static int generateFilmId() {
        return ++FilmId;
    }

    public static int generateUserId() {
        return ++UserId;
    }

    public static boolean isEmailExist(User user, Map<Integer,User>users) {
        int id = user.getId();
        for (User value : users.values()) {
            if (value.getId() != id) {
                if (user.getEmail().equals(value.getEmail())) {
                    return true;
                }
            }
        }
        return false;
    }
}
