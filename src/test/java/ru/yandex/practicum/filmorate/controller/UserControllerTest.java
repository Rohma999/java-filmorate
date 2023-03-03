package ru.yandex.practicum.filmorate.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final static ObjectMapper MAPPER = new ObjectMapper();

    @BeforeAll
    public static void beforeAll() {
        MAPPER.registerModule(new JavaTimeModule());
    }

    @Test
    public void shouldCreateUser() throws Exception {

        User user = new User("filmorate@mail.com", "MovieLover", LocalDate.of(1998, 11,
                12));
        user.setName("Alex");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotCreateUserWithInvalidEmail() throws Exception {

        User user = new User("ya gmail.ru", "Movie", LocalDate.of(1998, 10, 12));
        user.setName("Misha");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateUserWithInvalidLogin() throws Exception {

        User user = new User("filmorate@mail.com", " ", LocalDate.of(1998, 2, 14));
        user.setName("Nikita");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(user)))
                .andExpect(status().isBadRequest());

        user = new User("filmorate120@mail.com", "I love test", LocalDate.of(1998, 12, 10));

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateUserWithFutureBirthday() throws Exception {

        User user = new User("filmorate@mail.com", "Movid", LocalDate.of(2222, 12, 19));
        user.setName("Julia");

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
}