package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("GET /users — получение списка всех пользователей");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable long userId) {
        log.info("GET /users/{} — получение пользователя по ID", userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody UserDto userDto) {
        log.info("POST /users — создание нового пользователя: {}", userDto);
        return ResponseEntity.ok(userService.saveUser(userDto));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable long userId,
            @RequestBody UserDto userDto) {
        log.info("PATCH /users/{} — обновление пользователя: {}", userId, userDto);
        return ResponseEntity.ok(userService.updateUser(userId, userDto));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("DELETE /users/{} — удаление пользователя", userId);
        userService.deleteUser(userId);
    }
}
