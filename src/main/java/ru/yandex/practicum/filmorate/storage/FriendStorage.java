package ru.yandex.practicum.filmorate.storage;

public interface FriendStorage {

    int addFriend(long id, long userId);

    int deleteFriend(long id, long userId);
}
