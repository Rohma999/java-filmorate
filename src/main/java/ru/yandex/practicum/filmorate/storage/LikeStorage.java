package ru.yandex.practicum.filmorate.storage;

public interface LikeStorage {

    int addLike(long id, long userId);

    int deleteLike(long id, long userId);
}
