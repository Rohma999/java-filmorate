package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Data
@Builder
public class Film {
    private long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    @NotNull
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private int duration;
    @NotNull
    private Mpa mpa;
    private final Set<Long> likes = new HashSet<>();
    private final Set<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("rating_id", mpa.getId());
        return values;
    }

    @JsonProperty("genres")
    private void unpackGenres(Set<Map<String, Integer>> genres) {
        this.genres.addAll(genres.stream().map(g -> new Genre(g.get("id"), "")).collect(Collectors.toList()));
    }
}
