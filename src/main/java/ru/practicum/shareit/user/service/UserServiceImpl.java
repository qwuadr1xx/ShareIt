package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotUniqueEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(long userId) {
        User user = findUserOrThrow(userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        checkEmailUniqueness(userDto.getEmail());
        User user = UserMapper.toUser(userDto);
        User savedUser = userRepository.saveUser(user);
        return UserMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User existingUser = findUserOrThrow(userId);

        User updatedUser = existingUser.toBuilder()
                .name(userDto.getName() == null ? existingUser.getName() : userDto.getName())
                .email(userDto.getEmail() == null ? existingUser.getEmail() : userDto.getEmail())
                .build();

        userRepository.deleteUser(userId);
        checkEmailUniqueness(updatedUser.getEmail());
        userRepository.saveUser(updatedUser);

        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteUser(id);
    }

    private User findUserOrThrow(long userId) {
        return userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException(userId,
                        User.class.toString()));
    }

    private void checkEmailUniqueness(String email) {
        boolean emailExists = userRepository.getAllUsers().stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
        if (emailExists) {
            throw new NotUniqueEmailException(email);
        }
    }
}
