package ru.otus.hw.services;

import ru.otus.hw.models.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByLogin(String login);

    User save(User user);
}
