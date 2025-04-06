package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getUserById(long userId);

    List<User> getAllUsers();

    User saveUser(User user);

    void deleteUser(long userId);
}
