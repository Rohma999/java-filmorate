package ru.yandex.practicum.filmorate.model;


import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;
    @NotBlank
    @Email
    private final String email;
    @Pattern(regexp = "^\\w+$")
    @NotBlank
    private final String login;
    private String name;
    @PastOrPresent
    @NotNull
    private final LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();
}
