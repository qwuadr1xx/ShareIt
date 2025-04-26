package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOut saveNewBooking(@Valid @RequestBody BookingDtoIn bookingDtoIn,
                                        @RequestHeader(HEADER_USER_ID) long userId) {
        log.info("Получен POST-запрос на бронирование. userId: {}, itemId: {}", userId, bookingDtoIn.getItemId());
        return bookingService.saveNewBooking(bookingDtoIn, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut confirmBooking(@PathVariable long bookingId,
                                        @RequestHeader(HEADER_USER_ID) long userId,
                                        @RequestParam(name = "approved") boolean approve) {
        log.info("Получен PATCH-запрос на подтверждение бронирования. bookingId: {}, userId: {}, approved: {}", bookingId, userId, approve);
        return bookingService.confirmBooking(bookingId, userId, approve);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut getBooking(@PathVariable long bookingId,
                                    @RequestHeader(HEADER_USER_ID) long userId) {
        log.info("Получен GET-запрос на получение информации о бронировании. bookingId: {}, userId: {}", bookingId, userId);
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingDtoOut> getUserBookings(@RequestHeader(HEADER_USER_ID) long userId,
                                               @RequestParam(name = "state", required = false, defaultValue = "ALL") State state) {
        log.info("Получен GET-запрос на получение бронирований пользователя. userId: {}, state: {}", userId, state);
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getOwnerBookings(@RequestHeader(HEADER_USER_ID) long ownerId,
                                                @RequestParam(name = "state", required = false, defaultValue = "ALL") State state) {
        log.info("Получен GET-запрос на получение бронирований для владельца вещей. ownerId: {}, state: {}", ownerId, state);
        return bookingService.getOwnerBookings(ownerId, state);
    }
}
