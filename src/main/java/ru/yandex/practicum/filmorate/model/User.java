package ru.yandex.practicum.filmorate.model;


import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    @NotBlank
    @NotEmpty
    @Email
    private final String email;
    @NotBlank
    @Pattern(regexp = "^\\w+$")
    @NotEmpty
    private final String login;
    private String name;
    @PastOrPresent
    private final LocalDate birthday;
}
