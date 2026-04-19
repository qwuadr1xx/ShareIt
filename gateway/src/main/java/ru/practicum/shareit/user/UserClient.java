package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    public UserClient(@Value("${shareit-server.url}") String serverUrl) {
        super(RestClient.builder().baseUrl(serverUrl + API_PREFIX).build(), serverUrl + API_PREFIX);
    }

    public ResponseEntity<UserDto> saveUser(UserDto userDto) {
        return post("", userDto, UserDto.class);
    }

    public ResponseEntity<UserDto> updateUser(long userId, UserDto userDto) {
        return patch("/" + userId, userId, userDto, UserDto.class);
    }

    public void deleteUser(long userId) {
        delete("/" + userId, userId, Void.class);
    }

    public ResponseEntity<UserDto> getUserById(long userId) {
        return get("/" + userId, userId, UserDto.class);
    }

    public ResponseEntity<List<UserDto>> getAllUsers() {
        return getList("", UserDto[].class);
    }
}
