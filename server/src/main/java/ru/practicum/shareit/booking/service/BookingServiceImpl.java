package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    @Transactional
    public BookingDtoOut saveNewBooking(BookingDtoIn bookingDtoIn, Long userId) {
        if (!bookingDtoIn.getStart().isAfter(LocalDateTime.now()) ||
                !bookingDtoIn.getEnd().isAfter(bookingDtoIn.getStart())) {
            throw new BadRequestException(String.format(
                    "Некорректное начало %s или конец %s бронирования", bookingDtoIn.getStart(), bookingDtoIn.getEnd()),
                    "Booking");
        }

        Item item = itemService.findByIdOrThrow(bookingDtoIn.getItemId());
        User user = userService.findByIdOrThrow(userId);

        if (!item.getAvailable()) {
            throw new BadRequestException(String.format("Bookings с id %s невозможно забукить",
                    bookingDtoIn.getItemId()), "Booking");
        }

        if (item.getOwner().getId().equals(userId)) {
            throw new BadRequestException(String.format("Нельзя забукить собственную вещь с id %s",
                    item.getId()), "Booking");
        }

        Booking booking = BookingMapper.toBooking(bookingDtoIn).toBuilder()
                .item(item)
                .booker(user)
                .status(Status.WAITING)
                .build();

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDtoOut confirmBooking(Long bookingId, Long userId, Boolean approve) {
        Booking booking = bookingRepository
                .findByIdAndItemOwnerId(bookingId, userId)
                .orElseThrow(() -> new BadRequestException(
                        String.format("%s сущности User не является владельцем или букером", userId),
                        "Booking"));

        if (booking.getStatus().equals(Status.APPROVED) || booking.getStatus().equals(Status.REJECTED)) {
            throw new BadRequestException(
                    String.format("Статус бука с id %s уже финальный", bookingId), "Booking");
        }

        booking.setStatus(Boolean.TRUE.equals(approve) ? Status.APPROVED : Status.REJECTED);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDtoOut getBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository
                .findAccessibleBooking(bookingId, userId)
                .orElseThrow(() -> new NotFoundException(bookingId, "Booking"));

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDtoOut> getUserBookings(Long userId,
                                               State state,
                                               Long fromMillis,
                                               Integer size) {
        LocalDateTime from = Instant.ofEpochMilli(fromMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        PageRequest page = PageRequest.of(0, size);

        List<Booking> bookings;
        switch (state) {
            case ALL -> bookings = bookingRepository.findByUserAndStartAfter(userId, from, page);
            case CURRENT -> bookings = bookingRepository.findCurrentByUserAndStartAfter(userId, from, page);
            case PAST -> bookings = bookingRepository.findPastByUserAndStartAfter(userId, from, page);
            case FUTURE -> bookings = bookingRepository.findFutureByUserAndStartAfter(userId, from, page);
            case WAITING -> bookings = bookingRepository.findUserBookingsByStatusAndStartAfter(userId, Status.WAITING, from, page);
            case REJECTED -> bookings = bookingRepository.findUserBookingsByStatusAndStartAfter(userId, Status.REJECTED, from, page);
            default -> throw new IllegalArgumentException("Unknown state: " + state);
        }

        if (bookings.isEmpty()) {
            throw new NotFoundException(userId, "Bookings");
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDtoOut> getOwnerBookings(Long ownerId,
                                                State state,
                                                Long fromMillis,
                                                Integer size) {
        LocalDateTime from = Instant.ofEpochMilli(fromMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        PageRequest page = PageRequest.of(0, size);
        List<Booking> bookings;

        switch (state) {
            case ALL -> bookings = bookingRepository.findByOwnerAndStartAfter(ownerId, from, page);
            case CURRENT -> bookings = bookingRepository.findCurrentByOwnerAndStartAfter(ownerId, from, page);
            case PAST -> bookings = bookingRepository.findPastByOwnerAndStartAfter(ownerId, from, page);
            case FUTURE -> bookings = bookingRepository.findFutureByOwnerAndStartAfter(ownerId, from, page);
            case WAITING -> bookings = bookingRepository.findOwnerBookingsByStatusAndStartAfter(ownerId, Status.WAITING, from, page);
            case REJECTED -> bookings = bookingRepository.findOwnerBookingsByStatusAndStartAfter(ownerId, Status.REJECTED, from, page);
            default -> throw new IllegalArgumentException("Unknown state: " + state);
        }

        if (bookings.isEmpty()) {
            throw new NotFoundException(ownerId, "Bookings");
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }
}
