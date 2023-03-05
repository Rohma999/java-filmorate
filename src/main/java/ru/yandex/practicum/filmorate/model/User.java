package ru.yandex.practicum.filmorate.model;


import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private int id;
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
}
