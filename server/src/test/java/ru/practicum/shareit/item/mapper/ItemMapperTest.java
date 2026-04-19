package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoOutForRequest;
import ru.practicum.shareit.item.dto.ItemDtoOutShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemMapperTest {

    @Test
    void toItemDto_shouldMapItemToDtoWithBookingsAndComments() {
        User owner = User.builder()
                .id(1L)
                .name("Owner")
                .email("owner@mail.com")
                .build();

        Item item = Item.builder()
                .id(2L)
                .name("Item")
                .description("Item description")
                .available(true)
                .owner(owner)
                .build();

        BookingDtoOut lastBooking = BookingDtoOut.builder().id(100L).build();
        BookingDtoOut nextBooking = BookingDtoOut.builder().id(101L).build();
        CommentDtoOut comment = CommentDtoOut.builder().id(200L).text("nice").build();

        ItemDtoOut dto = ItemMapper.toItemDto(item, List.of(comment), lastBooking, nextBooking);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getName()).isEqualTo("Item");
        assertThat(dto.getDescription()).isEqualTo("Item description");
        assertThat(dto.getAvailable()).isTrue();
        assertThat(dto.getComments()).containsExactly(comment);
        assertThat(dto.getLastBooking()).isEqualTo(lastBooking);
        assertThat(dto.getNextBooking()).isEqualTo(nextBooking);
    }

    @Test
    void toItem_shouldMapDtoToItem() {
        ItemDtoIn dto = ItemDtoIn.builder()
                .name("Item")
                .description("Item description")
                .available(false)
                .build();

        Item item = ItemMapper.toItem(dto);

        assertThat(item).isNotNull();
        assertThat(item.getName()).isEqualTo("Item");
        assertThat(item.getDescription()).isEqualTo("Item description");
        assertThat(item.getAvailable()).isFalse();
    }

    @Test
    void toItemDtoShort_shouldMapItemToShortDto() {
        Item item = Item.builder()
                .id(3L)
                .name("Item")
                .description("Item description")
                .available(true)
                .build();

        ItemDtoOutShort dto = ItemMapper.toItemDtoShort(item);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(3L);
        assertThat(dto.getName()).isEqualTo("Item");
        assertThat(dto.getDescription()).isEqualTo("Item description");
        assertThat(dto.getAvailable()).isTrue();
    }

    @Test
    void toItemDtoOutForRequest_shouldMapItemToRequestDto() {
        User owner = User.builder()
                .id(5L)
                .name("Requestor")
                .email("requestor@gmail.com")
                .build();

        Item item = Item.builder()
                .id(6L)
                .name("Name")
                .owner(owner)
                .build();

        ItemDtoOutForRequest dto = ItemMapper.toItemDtoOutForRequest(item);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(6L);
        assertThat(dto.getName()).isEqualTo("Name");
        assertThat(dto.getOwnerId()).isEqualTo(5L);
    }
}
