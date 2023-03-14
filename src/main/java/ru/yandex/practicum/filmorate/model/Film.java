package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {
    private long id;
    @NotBlank
    private final String name;
    @Size(max = 200)
    @NotNull
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
    private final Set<Long> likes = new HashSet<>();


}
