package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.enums.State;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<BookingDtoOut> saveNewBooking(@Valid @RequestBody BookingDtoIn bookingDtoIn,
                                        @RequestHeader(HEADER_USER_ID) Long userId) {
        log.info("Получен POST-запрос на бронирование. userId: {}, itemId: {}", userId, bookingDtoIn);
        return bookingClient.bookItem(userId, bookingDtoIn);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDtoOut> confirmBooking(@PathVariable Long bookingId,
                                        @RequestHeader(HEADER_USER_ID) Long userId,
                                        @RequestParam(name = "approved") Boolean approve) {
        log.info("Получен PATCH-запрос на подтверждение бронирования. bookingId: {}, userId: {}, approved: {}",
                bookingId, userId, approve);
        return bookingClient.confirmBooking(bookingId, userId, approve);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDtoOut> getBooking(@PathVariable Long bookingId,
                                                    @RequestHeader(HEADER_USER_ID) long userId) {
        log.info("Получен GET-запрос на получение информации о бронировании. bookingId: {}, userId: {}",
                bookingId, userId);
        return bookingClient.getBooking(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<List<BookingDtoOut>> getUserBookings(@RequestHeader(HEADER_USER_ID) Long userId,
                                               @RequestParam(required = false, defaultValue = "ALL") State state,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") Long from,
                                               @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен GET-запрос на получение бронирований пользователя. userId: {}, state: {}", userId, state);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDtoOut>> getOwnerBookings(@RequestHeader(HEADER_USER_ID) Long ownerId,
                                                @RequestParam(required = false, defaultValue = "ALL") State state,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Long from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен GET-запрос на получение бронирований для владельца вещей. ownerId: {}, state: {}",
                ownerId, state);
        return bookingClient.getOwnerBookings(ownerId, state, from, size);
    }
}
