package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.enums.State;

import java.util.List;

public interface BookingService {
    BookingDtoOut saveNewBooking(BookingDtoIn bookingDtoIn, Long userId);

    BookingDtoOut confirmBooking(Long bookingId, Long userId, Boolean approve);

    BookingDtoOut getBooking(Long bookingId, Long userId);

    List<BookingDtoOut> getUserBookings(Long userId, State state);

    List<BookingDtoOut> getOwnerBookings(Long ownerId, State state);
}
