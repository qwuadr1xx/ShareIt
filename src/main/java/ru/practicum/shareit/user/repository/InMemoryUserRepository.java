package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {
    private long localId = 1;
    private final Map<Long, User> users;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User saveUser(User user) {
        User localUser = user.toBuilder()
                .id(generateId())
                .build();
        users.put(localUser.getId(), localUser);
        return localUser;
    }

    @Override
    public void deleteUser(long userId) {
        users.remove(userId);
    }

    private long generateId() {
        return localId++;
    }
}
