package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Name")
                .email("email@example.com")
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .name("Name")
                .email("email@example.com")
                .build();
    }

    @Test
    void getAllUsers_shouldReturnList() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("Name", result.getFirst().getName());
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_shouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);

        assertEquals("Name", result.getName());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_shouldThrowNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void saveUser_shouldSaveAndReturnUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = userService.saveUser(userDto);

        assertEquals("Name", result.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_shouldUpdateFields() {
        UserDto updatedDto = UserDto.builder()
                .name("NewName")
                .email(null)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDto result = userService.updateUser(1L, updatedDto);

        assertEquals("NewName", result.getName());
        assertEquals("email@example.com", result.getEmail()); // старое значение сохранилось
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteUser_shouldCallDelete() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void findByIdOrThrow_shouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findByIdOrThrow(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void findByIdOrThrow_shouldThrowNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.findByIdOrThrow(1L));

        assertEquals(1L, ex.getId());
        assertEquals("Item", ex.getEntity());
    }
}
