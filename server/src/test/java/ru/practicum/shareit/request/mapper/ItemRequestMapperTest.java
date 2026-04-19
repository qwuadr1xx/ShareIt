package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestMapperTest {

    @Test
    void toItemRequest_shouldMapDtoToEntity() {
        ItemRequestDtoIn dto = ItemRequestDtoIn.builder()
                .description("description")
                .build();

        User user = User.builder()
                .id(1L)
                .name("name")
                .email("example@gmail.com")
                .build();

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(dto, user);

        assertThat(itemRequest).isNotNull();
        assertThat(itemRequest.getDescription()).isEqualTo("description");
        assertThat(itemRequest.getRequestor()).isEqualTo(user);
        assertThat(itemRequest.getCreated()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void toItemRequestDtoOut_shouldMapEntityToDtoWithoutItems() {
        User user = User.builder()
                .id(2L)
                .name("name")
                .email("example@gmail.com")
                .build();

        ItemRequest request = ItemRequest.builder()
                .id(10L)
                .description("description")
                .created(LocalDateTime.now().minusDays(1))
                .requestor(user)
                .items(null)
                .build();

        ItemRequestDtoOut dto = ItemRequestMapper.toItemRequestDtoOut(request);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getDescription()).isEqualTo("description");
        assertThat(dto.getCreated()).isEqualTo(request.getCreated());
        assertThat(dto.getRequestor().getId()).isEqualTo(user.getId());
        assertThat(dto.getItems()).isNull();
    }

    @Test
    void toItemRequestDtoOut_shouldMapEntityToDtoWithItems() {
        User user = User.builder()
                .id(3L)
                .name("name")
                .email("example@gmail.com")
                .build();

        Item item = Item.builder()
                .id(20L)
                .name("name")
                .description("description")
                .available(true)
                .owner(user)
                .build();

        ItemRequest request = ItemRequest.builder()
                .id(30L)
                .description("description")
                .created(LocalDateTime.now())
                .requestor(user)
                .items(List.of(item))
                .build();

        ItemRequestDtoOut dto = ItemRequestMapper.toItemRequestDtoOut(request);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(30L);
        assertThat(dto.getItems()).hasSize(1);
        assertThat(dto.getItems().getFirst().getId()).isEqualTo(item.getId());
        assertThat(dto.getItems().getFirst().getName()).isEqualTo("name");
    }
}
