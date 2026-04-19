package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.enums.State;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class BookingClientTest {
    private BookingClient spyClient;
    private final long userId = 1L;
    private final long bookingId = 42L;
    private final long from = 0L;
    private final int size = 5;

    @BeforeEach
    void setUp() {
        BookingClient realClient = new BookingClient("http://localhost:8080");
        spyClient = spy(realClient);
    }

    @Test
    void bookItem_ShouldUsePostAndReturnDto() {
        BookingDtoIn inDto = BookingDtoIn.builder()
                .itemId(10L)
                .start(LocalDateTime.of(2025,1,1,10,0))
                .end(LocalDateTime.of(2025,1,2,10,0))
                .build();
        BookingDtoOut outDto = BookingDtoOut.builder().id(100L).build();

        doReturn(ResponseEntity.ok(outDto))
                .when(spyClient).bookItem(userId, inDto);

        ResponseEntity<BookingDtoOut> response = spyClient.bookItem(userId, inDto);

        assertEquals(100L, response.getBody().getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getBooking_ShouldUseGetAndReturnDto() {
        BookingDtoOut outDto = BookingDtoOut.builder().id(200L).build();

        doReturn(ResponseEntity.ok(outDto))
                .when(spyClient).getBooking(bookingId, userId);

        ResponseEntity<BookingDtoOut> response = spyClient.getBooking(bookingId, userId);
        assertEquals(200L, response.getBody().getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void confirmBooking_ShouldUsePatchAndReturnDto() {
        BookingDtoOut outDto = BookingDtoOut.builder().id(300L).build();

        doReturn(ResponseEntity.ok(outDto))
                .when(spyClient).confirmBooking(bookingId, userId, true);

        ResponseEntity<BookingDtoOut> response = spyClient.confirmBooking(bookingId, userId, true);
        assertEquals(300L, response.getBody().getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void getBookings_ShouldUseGetListAndReturnList() {
        BookingDtoOut element = BookingDtoOut.builder().id(400L).build();
        List<BookingDtoOut> stubList = List.of(element);

        doReturn(ResponseEntity.ok(stubList))
                .when(spyClient).getBookings(userId, State.ALL, from, size);

        ResponseEntity<List<BookingDtoOut>> response = spyClient.getBookings(userId, State.ALL, from, size);
        assertEquals(1, response.getBody().size());
        assertEquals(400L, response.getBody().getFirst().getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getOwnerBookings_ShouldUseGetListAndReturnList() {
        BookingDtoOut element = BookingDtoOut.builder().id(500L).build();
        List<BookingDtoOut> stubList = List.of(element);

        doReturn(ResponseEntity.ok(stubList))
                .when(spyClient).getOwnerBookings(userId, State.ALL, from, size);

        ResponseEntity<List<BookingDtoOut>> response = spyClient.getOwnerBookings(userId, State.ALL, from, size);
        assertEquals(1, response.getBody().size());
        assertEquals(500L, response.getBody().getFirst().getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
