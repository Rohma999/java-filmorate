package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;


import java.util.Collection;

@Slf4j
@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(@Qualifier("MpaDbStorage") MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Collection<Mpa> getAllMpa() {
        Collection<Mpa> allMpa = mpaStorage.findAll();
        log.info("Передаем в контроллер все рейтинги : {}", allMpa);
        return allMpa;
    }

    public Mpa getMpa(int id) {
        Mpa mpa = mpaStorage.getMpa(id);
        log.info("Передаем в контроллер рейтинг с id {} : {}", id, mpa);
        return mpa;
    }
}
