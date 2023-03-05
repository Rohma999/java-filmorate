package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.EmailAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.IdGenerator;

import java.time.LocalDate;

public class UserControllerTest {

    UserController userController;

    @BeforeEach
    public void beforeAll() {
        userController = new UserController(new IdGenerator());
    }

    @Test
    public void shouldCreateUserWithoutName() {

        User user = new User("filmorate@mail.com", "MovieLover", LocalDate.of(1998, 11,
                12));

        User user1 = new User("filmorate@mail.com", "MovieLover", LocalDate.of(1998, 11,
                12));
        user1.setName("MovieLover");
        user1.setId(1);
        userController.create(user);
        assertEquals(user1, userController.findAll().get(0));

    }

    @Test
    public void shouldNotUpdateUserWithBadId() {

        User user = new User("filmorate@mail.com", "Movie", LocalDate.of(1998, 10, 12));
        user.setName("Misha");
        user.setId(9);
        assertThrows(ElementDoesNotExistException.class, () -> userController.put(user));
    }

    @Test
    public void shouldNotCrateUserIfEmailAlreadyExist() {

        User user = new User("filmorate@mail.com", "Movie", LocalDate.of(1998, 10, 12));
        userController.create(user);

        assertThrows(EmailAlreadyExistException.class, () -> userController.create(user));
    }

    @Test
    public void shouldNotUpdateUserIfEmailAlreadyExist() {

        User user = new User("filmorate@mail.com", "Movie", LocalDate.of(1998, 10, 12));
        user.setName("Misha");
        userController.create(user);
        User user1 = new User("movie@mail.com", "Gena", LocalDate.of(1998, 10, 12));
        userController.create(user1);
        user.setId(2);

        assertThrows(EmailAlreadyExistException.class, () -> userController.put(user));
    }
}