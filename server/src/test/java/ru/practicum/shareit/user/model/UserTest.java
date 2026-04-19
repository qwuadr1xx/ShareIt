package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void userBuilderShouldCreateCorrectEntity() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("example@gmail.com")
                .build();

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("name");
        assertThat(user.getEmail()).isEqualTo("example@gmail.com");
    }
}
