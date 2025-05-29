package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingMapperTest {

    @Test
    void toBookingDto_shouldMapBookingToDto() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        User booker = User.builder()
                .id(1L)
                .name("Booker")
                .email("booker@gmail.com")
                .build();

        Item item = Item.builder()
                .id(2L)
                .name("Item")
                .description("Item description")
                .available(true)
                .build();

        Booking booking = Booking.builder()
                .id(10L)
                .start(start)
                .end(end)
                .status(Status.APPROVED)
                .booker(booker)
                .item(item)
                .build();

        BookingDtoOut dto = BookingMapper.toBookingDto(booking);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getStart()).isEqualTo(start);
        assertThat(dto.getEnd()).isEqualTo(end);
        assertThat(dto.getStatus()).isEqualTo(Status.APPROVED);
        assertThat(dto.getBooker()).isNotNull();
        assertThat(dto.getBooker().getId()).isEqualTo(booker.getId());
        assertThat(dto.getItem()).isNotNull();
        assertThat(dto.getItem().getId()).isEqualTo(item.getId());
    }

    @Test
    void toBooking_shouldMapDtoToBooking() {
        LocalDateTime start = LocalDateTime.now().plusDays(3);
        LocalDateTime end = LocalDateTime.now().plusDays(4);

        BookingDtoIn dtoIn = BookingDtoIn.builder()
                .start(start)
                .end(end)
                .itemId(5L)
                .build();

        Booking booking = BookingMapper.toBooking(dtoIn);

        assertThat(booking).isNotNull();
        assertThat(booking.getStart()).isEqualTo(start);
        assertThat(booking.getEnd()).isEqualTo(end);
    }

    @Test
    void toBookingDto_shouldReturnNull_whenBookingIsNull() {
        assertThat(BookingMapper.toBookingDto(null)).isNull();
    }
}
