package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTest {
    private final UserService userService;


    @BeforeEach
    public void setUp() {
        User user = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
        User friend = User.builder()
                .login("friend")
                .name("friend adipisicing")
                .email("friend@mail.ru")
                .birthday(LocalDate.of(1976, 8, 20))
                .build();
        User commonFriend = User.builder()
                .login("common")
                .email("friend@common.ru")
                .birthday(LocalDate.of(2000, 8, 20))
                .build();

        userService.create(user);
        userService.create(friend);
        userService.create(commonFriend);
    }

    @Test
    public void shouldUpdateUser() {
        User updatedUser = User.builder()
                .id(1L)
                .login("doloreUpdate")
                .name("est adipisicing")
                .email("mail@yandex.ru")
                .birthday(LocalDate.of(1976, 9, 20))
                .build();

        User user = userService.put(updatedUser);

        assertEquals(updatedUser.getLogin(), user.getLogin());
        assertEquals(updatedUser.getName(), user.getName());
        assertEquals(updatedUser.getEmail(), user.getEmail());
        assertEquals(updatedUser.getBirthday(), user.getBirthday());
    }

    @Test
    public void shouldReturnUserById() {
        User user = userService.getUserById(1);

        assertNotNull(user);

        assertEquals(user.getLogin(), "dolore");
    }

    @Test
    public void shouldReturnAllUsers() {
        Collection<User> users = userService.findAll();
        assertEquals(3, users.size());
    }

    @Test
    public void shouldAddToFriends() {
        userService.addFriend(1, 2);

        Collection<User> friends = userService.findUserFriends(1);
        assertEquals(1, friends.size());
    }


    @Test
    public void shouldFindCommonFriends() {
        userService.addFriend(1, 3);
        userService.addFriend(2, 3);
        List<User> commonFriends = new ArrayList<>(userService.findCommonUserFriends(1, 2));
        User commonFriend = commonFriends.get(0);

        assertEquals(1, commonFriends.size());
        assertEquals("common", commonFriend.getLogin());
    }

    @Test
    public void shouldDeleteFromFriends() {
        userService.addFriend(1, 2);
        userService.addFriend(1, 3);
        userService.deleteFriend(1, 2);
        List<User> friends = new ArrayList<>(userService.findUserFriends(1));

        assertEquals(1, friends.size());
    }
}

