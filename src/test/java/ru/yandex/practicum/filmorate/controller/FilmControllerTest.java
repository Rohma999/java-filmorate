package ru.yandex.practicum.filmorate.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final static ObjectMapper MAPPER = new ObjectMapper();

    @BeforeAll
    public static void beforeAll() {
        MAPPER.registerModule(new JavaTimeModule());
    }

    @Test
    public void shouldCreateFilmAndUpdate() throws Exception {

        Film film = new Film("Зеленая миля", "Test description",
                LocalDate.of(2000, 1, 1), 200);

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(film)))
                .andExpect(status().isOk());

        Film film1 = new Film("Зеленая миля", "Топ фильм",
                LocalDate.of(2000, 1, 1), 200);
        film1.setId(1);

        this.mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(film1)))
                .andExpect(status().isOk());

        
    }

    @Test
    public void shouldNotCreateFilmWithInvalidDate() throws Exception {

        Film film = new Film("Форрест Гамп", "Сидя на автобусной остановке, Форрест Гамп — " +
                "не очень умный, но добрый и открытый парень — рассказывает случайным встречным историю своей " +
                "необыкновенной жизни.", LocalDate.of(1000, 1, 1), 200);

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateFilmWithEmptyName() throws Exception {

        Film film = new Film("", "Test description", LocalDate.of(1000, 1, 1), 200);

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateFilmWithLongDescription() throws Exception {

        Film film = new Film("Криминальное чтиво", "Двое бандитов Винсент Вега и Джулс Винфилд ведут" +
                " философские беседы в перерывах между разборками и решением проблем с должниками криминального босса " +
                "Марселласа Уоллеса.В первой истории Винсент проводит незабываемый вечер с женой Марселласа Мией. " +
                "Во второй рассказывается о боксёре Бутче Кулидже, купленном Уоллесом, чтобы сдать бой. " +
                "В третьей истории Винсент и Джулс по нелепой случайности попадают в неприятности.",
                LocalDate.of(1994, 1, 1), 154);

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateFilmWithNegativeDuration() throws Exception {

        Film film = new Film("Назад в будущее ", "Подросток Марти с помощью машины времени, " +
                "сооружённой его другом-профессором доком Брауном, попадает из 80-х в далекие 50-е. " +
                "Там он встречается со своими будущими родителями, ещё подростками, и другом-профессором, совсем молодым.",
                LocalDate.of(1985, 1, 1), -200);

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }
}