package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTest {

    @Test
    void itemBuilderShouldCreateCorrectItem() {
        User owner = User.builder()
                .id(1L)
                .name("Owner")
                .email("owner@mail.com")
                .build();

        ItemRequest request = ItemRequest.builder()
                .id(2L)
                .description("description")
                .build();

        Item item = Item.builder()
                .id(2L)
                .name("Item")
                .description("Item description")
                .available(true)
                .owner(owner)
                .request(request)
                .build();

        assertThat(item.getId()).isEqualTo(2L);
        assertThat(item.getName()).isEqualTo("Item");
        assertThat(item.getDescription()).isEqualTo("Item description");
        assertThat(item.getAvailable()).isTrue();
        assertThat(item.getOwner()).isEqualTo(owner);
        assertThat(item.getRequest()).isEqualTo(request);
    }
}
