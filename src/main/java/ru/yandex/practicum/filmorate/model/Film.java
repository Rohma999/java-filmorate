package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;


@Data
public class Film {
    private int id;
    @NotBlank
    private final String name;
    @Size(max = 200)
    @NotNull
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
}
