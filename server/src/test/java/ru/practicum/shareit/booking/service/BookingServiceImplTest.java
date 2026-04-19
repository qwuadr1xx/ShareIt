package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User user;
    private User booker;
    private Item item;
    private BookingDtoIn bookingDtoIn;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Test User")
                .build();

        booker = User.builder()
                .id(2L)
                .name("Test Booker")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Test Item")
                .available(true)
                .owner(user)
                .build();

        bookingDtoIn = BookingDtoIn.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        // Инициализация самого бронирования
        booking = Booking.builder()
                .id(1L)
                .start(bookingDtoIn.getStart())
                .end(bookingDtoIn.getEnd())
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();
    }

    @Test
    void saveNewBooking_ShouldCreateBooking() {
        when(itemService.findByIdOrThrow(1L)).thenReturn(item);
        when(userService.findByIdOrThrow(2L)).thenReturn(booker);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDtoOut result = bookingService.saveNewBooking(bookingDtoIn, booker.getId());

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Status.WAITING, result.getStatus());
    }

    @Test
    void confirmBooking_ShouldUpdateBookingStatusToApproved() {
        when(bookingRepository.findByIdAndItemOwnerId(1L, user.getId())).thenReturn(Optional.of(booking));

        BookingDtoOut result = bookingService.confirmBooking(1L, user.getId(), true);

        assertNotNull(result);
        assertEquals(Status.APPROVED, result.getStatus());
    }

    @Test
    void saveNewBooking_ShouldThrowBadRequest_WhenItemIsNotAvailable() {
        item.setAvailable(false);

        when(itemService.findByIdOrThrow(1L)).thenReturn(item);
        when(userService.findByIdOrThrow(2L)).thenReturn(booker);

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> bookingService.saveNewBooking(bookingDtoIn, booker.getId()));

        assertEquals("Bookings с id 1 невозможно забукить", ex.getError());
    }

    @Test
    void saveNewBooking_ShouldThrowBadRequest_WhenDatesInvalid() {
        bookingDtoIn.setEnd(LocalDateTime.now().minusDays(1));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> bookingService.saveNewBooking(bookingDtoIn, booker.getId()));

        assertEquals(
                String.format("Некорректное начало %s или конец %s бронирования",
                        bookingDtoIn.getStart(), bookingDtoIn.getEnd()),
                ex.getError()
        );
    }

    @Test
    void getBooking_ShouldReturnBooking() {
        when(bookingRepository.findAccessibleBooking(1L, booker.getId())).thenReturn(Optional.of(booking));

        BookingDtoOut result = bookingService.getBooking(1L, booker.getId());

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getBooking_ShouldThrowNotFound_WhenBookingDoesNotExist() {
        when(bookingRepository.findAccessibleBooking(1L, booker.getId()))
                .thenThrow(new NotFoundException(1L, "Booking"));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.getBooking(1L, booker.getId()));

        assertEquals(1L, exception.getId());
    }

    @Test
    void getUserBookings_StateAll_ShouldReturnBookings() {
        when(bookingRepository.findByUserAndStartAfter(eq(2L), any(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> result = bookingService.getUserBookings(2L, State.ALL, 0L, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1))
                .findByUserAndStartAfter(eq(2L), any(), any());
    }

    @Test
    void getUserBookings_StateCurrent_ShouldReturnBookings() {
        when(bookingRepository.findCurrentByUserAndStartAfter(eq(2L), any(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> result = bookingService.getUserBookings(2L, State.CURRENT, 0L, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1))
                .findCurrentByUserAndStartAfter(eq(2L), any(), any());
    }

    @Test
    void getUserBookings_StatePast_ShouldReturnBookings() {
        when(bookingRepository.findPastByUserAndStartAfter(eq(2L), any(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> result = bookingService.getUserBookings(2L, State.PAST, 0L, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1))
                .findPastByUserAndStartAfter(eq(2L), any(), any());
    }

    @Test
    void getUserBookings_StateFuture_ShouldReturnBookings() {
        when(bookingRepository.findFutureByUserAndStartAfter(eq(2L), any(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> result = bookingService.getUserBookings(2L, State.FUTURE, 0L, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1))
                .findFutureByUserAndStartAfter(eq(2L), any(), any());
    }

    @Test
    void getUserBookings_StateWaiting_ShouldReturnBookings() {
        when(bookingRepository.findUserBookingsByStatusAndStartAfter(eq(2L), eq(Status.WAITING), any(), any()))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> result = bookingService.getUserBookings(2L, State.WAITING, 0L, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1))
                .findUserBookingsByStatusAndStartAfter(eq(2L), eq(Status.WAITING), any(), any());
    }

    @Test
    void getUserBookings_StateRejected_ShouldReturnBookings() {
        when(bookingRepository.findUserBookingsByStatusAndStartAfter(eq(2L), eq(Status.REJECTED), any(), any()))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> result = bookingService.getUserBookings(2L, State.REJECTED, 0L, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1))
                .findUserBookingsByStatusAndStartAfter(eq(2L), eq(Status.REJECTED), any(), any());
    }

    @Test
    void getUserBookings_EmptyList_ShouldThrowNotFound() {
        when(bookingRepository.findByUserAndStartAfter(eq(2L), any(), any()))
                .thenReturn(Collections.emptyList());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.getUserBookings(2L, State.ALL, 0L, 10));

        assertEquals(2L, exception.getId());
        verify(bookingRepository, times(1))
                .findByUserAndStartAfter(eq(2L), any(), any());
    }

    @Test
    void getOwnerBookings_StateAll_ShouldReturnBookings() {
        when(bookingRepository.findByOwnerAndStartAfter(eq(1L), any(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> result = bookingService.getOwnerBookings(1L, State.ALL, 0L, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1))
                .findByOwnerAndStartAfter(eq(1L), any(), any());
    }

    @Test
    void getOwnerBookings_StateCurrent_ShouldReturnBookings() {
        when(bookingRepository.findCurrentByOwnerAndStartAfter(eq(1L), any(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> result = bookingService.getOwnerBookings(1L, State.CURRENT, 0L, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1))
                .findCurrentByOwnerAndStartAfter(eq(1L), any(), any());
    }

    @Test
    void getOwnerBookings_StatePast_ShouldReturnBookings() {
        when(bookingRepository.findPastByOwnerAndStartAfter(eq(1L), any(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> result = bookingService.getOwnerBookings(1L, State.PAST, 0L, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1))
                .findPastByOwnerAndStartAfter(eq(1L), any(), any());
    }

    @Test
    void getOwnerBookings_StateFuture_ShouldReturnBookings() {
        when(bookingRepository.findFutureByOwnerAndStartAfter(eq(1L), any(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> result = bookingService.getOwnerBookings(1L, State.FUTURE, 0L, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1))
                .findFutureByOwnerAndStartAfter(eq(1L), any(), any());
    }

    @Test
    void getOwnerBookings_StateWaiting_ShouldReturnBookings() {
        when(bookingRepository.findOwnerBookingsByStatusAndStartAfter(eq(1L), eq(Status.WAITING), any(), any()))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> result = bookingService.getOwnerBookings(1L, State.WAITING, 0L, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1))
                .findOwnerBookingsByStatusAndStartAfter(eq(1L), eq(Status.WAITING), any(), any());
    }

    @Test
    void getOwnerBookings_StateRejected_ShouldReturnBookings() {
        when(bookingRepository.findOwnerBookingsByStatusAndStartAfter(eq(1L), eq(Status.REJECTED), any(), any()))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> result = bookingService.getOwnerBookings(1L, State.REJECTED, 0L, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1))
                .findOwnerBookingsByStatusAndStartAfter(eq(1L), eq(Status.REJECTED), any(), any());
    }

    @Test
    void getOwnerBookings_EmptyList_ShouldThrowNotFound() {
        when(bookingRepository.findByOwnerAndStartAfter(eq(1L), any(), any()))
                .thenReturn(Collections.emptyList());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.getOwnerBookings(1L, State.ALL, 0L, 10));

        assertEquals(1L, exception.getId());
        verify(bookingRepository, times(1))
                .findByOwnerAndStartAfter(eq(1L), any(), any());
    }
}
