package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


public class BookingTest {
    @Test
    void bookingBuilderShouldCreateRightBooking() {
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

        assertThat(booking.getId()).isEqualTo(10L);
        assertThat(booking.getStart()).isEqualTo(start);
        assertThat(booking.getEnd()).isEqualTo(end);
        assertThat(booking.getStatus()).isEqualTo(Status.APPROVED);
        assertThat(booking.getBooker()).isEqualTo(booker);
        assertThat(booking.getItem()).isEqualTo(item);
    }
}
