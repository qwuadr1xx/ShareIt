package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class UserClientTest {
    private UserClient spyClient;
    private final long userId = 1L;

    @BeforeEach
    void setUp() {
        UserClient realClient = new UserClient("http://localhost:8080");
        spyClient = spy(realClient);
    }

    @Test
    void saveUser_ShouldUsePostAndReturnDto() {
        UserDto inDto = UserDto.builder()
                .id(null)
                .name("John")
                .email("john@example.com")
                .build();
        UserDto outDto = UserDto.builder()
                .id(userId)
                .name("John")
                .email("john@example.com")
                .build();

        doReturn(ResponseEntity.ok(outDto))
                .when(spyClient).saveUser(inDto);

        ResponseEntity<UserDto> resp = spyClient.saveUser(inDto);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(userId, resp.getBody().getId());
        assertEquals("John", resp.getBody().getName());
    }

    @Test
    void updateUser_ShouldUsePatchAndReturnDto() {
        UserDto inDto = UserDto.builder()
                .id(userId)
                .name("Jane")
                .email("jane@example.com")
                .build();
        UserDto outDto = UserDto.builder()
                .id(userId)
                .name("Jane")
                .email("jane@example.com")
                .build();

        doReturn(ResponseEntity.ok(outDto))
                .when(spyClient).updateUser(userId, inDto);

        ResponseEntity<UserDto> resp = spyClient.updateUser(userId, inDto);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("Jane", resp.getBody().getName());
    }


    @Test
    void getUserById_ShouldUseGetAndReturnDto() {
        UserDto outDto = UserDto.builder()
                .id(userId)
                .name("John")
                .email("john@example.com")
                .build();

        doReturn(ResponseEntity.ok(outDto))
                .when(spyClient).getUserById(userId);

        ResponseEntity<UserDto> resp = spyClient.getUserById(userId);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(userId, resp.getBody().getId());
    }

    @Test
    void getAllUsers_ShouldUseGetListAndReturnList() {
        UserDto user = UserDto.builder()
                .id(userId)
                .name("John")
                .email("john@example.com")
                .build();
        List<UserDto> stub = List.of(user);

        doReturn(ResponseEntity.ok(stub))
                .when(spyClient).getAllUsers();

        ResponseEntity<List<UserDto>> resp = spyClient.getAllUsers();

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1, resp.getBody().size());
        assertEquals(userId, resp.getBody().getFirst().getId());
    }
}
