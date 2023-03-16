package ru.yandex.practicum.filmorate.utils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class IdGenerator {
    private int id = 0;

    public int generateId() {
        return ++id;
    }
}
