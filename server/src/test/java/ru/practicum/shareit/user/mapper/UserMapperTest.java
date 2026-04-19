package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoForItemRequest;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    @Test
    void toUserDto_shouldMapUserToDto() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("example@gmail.com")
                .build();

        UserDto dto = UserMapper.toUserDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("name");
        assertThat(dto.getEmail()).isEqualTo("example@gmail.com");
    }

    @Test
    void toUser_shouldMapDtoToUser() {
        UserDto dto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("example@gmail.com")
                .build();

        User user = UserMapper.toUser(dto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("name");
        assertThat(user.getEmail()).isEqualTo("example@gmail.com");
    }

    @Test
    void toUserDtoForItemRequest_shouldMapUserToDto() {
        User user = User.builder()
                .id(2L)
                .name("name")
                .email("example@gmail.com")
                .build();

        UserDtoForItemRequest dto = UserMapper.toUserDtoForItemRequest(user);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getName()).isEqualTo("name");
    }
}
