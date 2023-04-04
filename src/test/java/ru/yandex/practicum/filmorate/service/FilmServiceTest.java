package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ElementDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmServiceTest {

    private final FilmService filmService;

    private final UserService userService;


    @BeforeEach
    public void setUpTest() {
        Film film = Film.builder()
                .name("nisi eiusmod")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .description("adipisicing")
                .mpa(new Mpa(1, null))
                .build();
        Film film2 = Film.builder()
                .name("New film")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .description("New film about friends")
                .mpa(new Mpa(3, null))
                .build();
        film2.getGenres().addAll((new HashSet<>(Set.of(new Genre(1, null)))));
        User user = User.builder()
                .login("user")
                .name("login")
                .email("email@email.ru")
                .birthday(LocalDate.of(1990, 7, 2))
                .build();


        filmService.create(film);
        filmService.create(film2);
        userService.create(user);

    }

    @Test
    public void shouldFindFilmById() {
        Film film = filmService.getFilmById(1);

        assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void shouldFindAllFilms() {
        assertEquals(2, filmService.findAll().size());
    }

    @Test
    public void shouldUpdateFilm() {
        Film updatedFilm = Film.builder()
                .id(1L)
                .name("Film Updated")
                .releaseDate(LocalDate.of(1989, 4, 17))
                .duration(190)
                .description("New film update description")
                .mpa(new Mpa(2, "PG"))
                .build();
        updatedFilm.getGenres().addAll((new HashSet<>(Set.of(new Genre(1, "Комедия"),
                new Genre(2, "Драма"), new Genre(3, "Мультфильм")))));
        Film actualFilm = filmService.put(updatedFilm);

        assertEquals(actualFilm.getName(), updatedFilm.getName());
        assertEquals(actualFilm.getReleaseDate(), updatedFilm.getReleaseDate());
        assertEquals(actualFilm.getDuration(), updatedFilm.getDuration());
        assertEquals(actualFilm.getDescription(), updatedFilm.getDescription());
        assertEquals(actualFilm.getMpa(), updatedFilm.getMpa());
        assertEquals(actualFilm.getGenres(), updatedFilm.getGenres());
    }

    @Test
    public void shouldReturnPopularFilm() {
        filmService.addLike(2, 1);
        Collection<Film> films = filmService.getPopularFilms(2);

        assertThat(films.size()).isEqualTo(2);
        assertThat(films.stream().mapToLong(Film::getId).toArray())
                .isEqualTo(new long[]{2, 1});

    }

    @Test
    public void shouldThrowFilmNotFountException() {
        assertThrows(ElementDoesNotExistException.class, () -> filmService.getFilmById(999L));

    }

    @Test
    public void shouldGetGenreById() {
        Genre genre = filmService.getGenre(1);

        assertEquals(new Genre(1, "Комедия"), genre);
    }

    @Test
    public void shouldReturnAllGenres() {
        Collection<Genre> genres = filmService.getGenres();

        assertEquals(6, genres.size());
    }

    @Test
    public void shoutThrowGenreNotFoundException() {
        assertThrows(ElementDoesNotExistException.class, () -> filmService.getGenre(999));
    }

    @Test
    public void shouldGetMpaById() {
        Mpa mpa = filmService.getMpa(1);

        assertEquals(new Mpa(1, "G"), mpa);
    }

    @Test
    public void shoutThrowMpaNotFoundException() {
        assertThrows(ElementDoesNotExistException.class, () -> filmService.getMpa(999));
    }

    @Test
    public void shouldReturnAllMpa() {
        Collection<Mpa> allMpa = filmService.getAllMpa();

        assertEquals(5, allMpa.size());
    }
}
