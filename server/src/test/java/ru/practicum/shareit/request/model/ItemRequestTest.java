package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestTest {

    @Test
    void builderShouldCreateItemRequestCorrectly() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("example@gmail.com")
                .build();

        Item item = Item.builder()
                .id(10L)
                .name("name")
                .description("description")
                .available(true)
                .owner(user)
                .build();

        LocalDateTime created = LocalDateTime.now();

        ItemRequest request = ItemRequest.builder()
                .id(100L)
                .description("description")
                .created(created)
                .requestor(user)
                .items(List.of(item))
                .build();

        assertThat(request.getId()).isEqualTo(100L);
        assertThat(request.getDescription()).isEqualTo("description");
        assertThat(request.getCreated()).isEqualTo(created);
        assertThat(request.getRequestor()).isEqualTo(user);
        assertThat(request.getItems()).containsExactly(item);
    }
}
