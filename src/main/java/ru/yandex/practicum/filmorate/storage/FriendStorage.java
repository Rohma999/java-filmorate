package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendStorage {

    int addFriend(long id, long userId);

    int deleteFriend(long id, long userId);

     Collection<User> findCommonUserFriends(long id, long otherId);

    Collection<User> findUserFriends(long id);
}
