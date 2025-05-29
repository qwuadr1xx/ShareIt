package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = ShareItServer.class)
class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User owner;
    private User booker;
    private Item item;
    private Booking pastBooking;
    private Booking currentBooking;
    private Booking futureBooking;
    private Booking rejectedBooking;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();

        owner = User.builder()
                .name("Owner")
                .email("owner@example.com")
                .build();
        userRepository.save(owner);

        booker = User.builder()
                .name("Booker")
                .email("booker@example.com")
                .build();
        userRepository.save(booker);

        item = Item.builder()
                .name("Drill")
                .description("Powerful drill")
                .available(true)
                .owner(owner)
                .build();
        itemRepository.save(item);

        LocalDateTime now = LocalDateTime.now();

        pastBooking = Booking.builder()
                .start(now.minusDays(10))
                .end(now.minusDays(5))
                .item(item)
                .booker(booker)
                .status(Status.APPROVED)
                .build();

        currentBooking = Booking.builder()
                .start(now.minusHours(2))
                .end(now.plusHours(2))
                .item(item)
                .booker(booker)
                .status(Status.APPROVED)
                .build();

        futureBooking = Booking.builder()
                .start(now.plusHours(1))
                .end(now.plusHours(3))
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();

        rejectedBooking = Booking.builder()
                .start(now.plusHours(2))
                .end(now.plusHours(4))
                .item(item)
                .booker(booker)
                .status(Status.REJECTED)
                .build();

        bookingRepository.saveAll(List.of(
                pastBooking,
                currentBooking,
                futureBooking,
                rejectedBooking
        ));
    }

    @Test
    void findByIdAndItemOwnerId_ShouldReturnBookingWhenExists() {
        Optional<Booking> result = bookingRepository.findByIdAndItemOwnerId(
                currentBooking.getId(),
                owner.getId()
        );

        assertTrue(result.isPresent());
        assertEquals(currentBooking.getId(), result.get().getId());
    }

    @Test
    void findAccessibleBooking_ShouldReturnForOwner() {
        Optional<Booking> result = bookingRepository.findAccessibleBooking(
                currentBooking.getId(),
                owner.getId()
        );

        assertTrue(result.isPresent());
        assertEquals(currentBooking.getId(), result.get().getId());
    }

    @Test
    void findAccessibleBooking_ShouldReturnForBooker() {
        Optional<Booking> result = bookingRepository.findAccessibleBooking(
                currentBooking.getId(),
                booker.getId()
        );

        assertTrue(result.isPresent());
        assertEquals(currentBooking.getId(), result.get().getId());
    }

    @Test
    void findByUserAndStartAfter_ShouldReturnAllAfterGivenDate() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(1);
        List<Booking> list = bookingRepository.findByUserAndStartAfter(
                booker.getId(), cutoff, PageRequest.of(0, 10));

        assertEquals(3, list.size());
        assertTrue(list.stream().noneMatch(b -> b.getStart().isBefore(cutoff)));
    }

    @Test
    void findCurrentByUserAndStartAfter_ShouldReturnCurrentBookings() {
        List<Booking> list = bookingRepository.findCurrentByUserAndStartAfter(
                booker.getId(), LocalDateTime.now().minusDays(30), PageRequest.of(0, 10));
        assertEquals(2, list.size());

        assertEquals(currentBooking.getId(), list.getFirst().getId());
    }

    @Test
    void findPastByUserAndStartAfter_ShouldReturnPastBookings() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Booking> result = bookingRepository.findPastByUserAndStartAfter(
                booker.getId(),
                LocalDateTime.now().minusDays(30),
                pageable
        );

        assertEquals(1, result.size());
        assertEquals(pastBooking.getId(), result.getFirst().getId());
        assertTrue(result.getFirst().getEnd().isBefore(LocalDateTime.now()));
    }

    @Test
    void findFutureByUserAndStartAfter_ShouldReturnFutureBookings() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Booking> result = bookingRepository.findFutureByUserAndStartAfter(
                booker.getId(),
                LocalDateTime.now().minusDays(1),
                pageable
        );

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(b -> b.getStart().isAfter(LocalDateTime.now())));
    }

    @Test
    void findUserBookingsByStatusAndStartAfter_ShouldReturnFiltered() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Booking> result = bookingRepository.findUserBookingsByStatusAndStartAfter(
                booker.getId(),
                Status.WAITING,
                LocalDateTime.now().minusDays(1),
                pageable
        );

        assertEquals(1, result.size());
        assertEquals(futureBooking.getId(), result.getFirst().getId());
        assertEquals(Status.WAITING, result.getFirst().getStatus());
    }

    @Test
    void findByOwnerAndStartAfter_ShouldReturnForOwner() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Booking> result = bookingRepository.findByOwnerAndStartAfter(
                owner.getId(),
                LocalDateTime.now().minusDays(30),
                pageable
        );

        assertEquals(4, result.size());
    }

    @Test
    void firstAndLastBookingByItemId_viaStream() {
        List<Booking> all = bookingRepository.findByOwnerAndStartAfter(
                owner.getId(), LocalDateTime.now().minusYears(1), PageRequest.of(0, 10));

        Booking first = all.stream()
                .min(Comparator.comparing(Booking::getStart))
                .orElseThrow();
        Booking last = all.stream()
                .max(Comparator.comparing(Booking::getStart))
                .orElseThrow();

        assertEquals(pastBooking.getId(), first.getId());
        assertEquals(rejectedBooking.getId(), last.getId());
    }

    @Test
    void findPastByUser_ShouldReturnPastBookings() {
        List<Booking> result = bookingRepository.findPastByUser(booker.getId());
        assertEquals(1, result.size());
        assertEquals(pastBooking.getId(), result.getFirst().getId());
    }

    @Test
    void findOwnerBookingsByStatusAndStartAfter_ShouldReturnRejected() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Booking> result = bookingRepository.findOwnerBookingsByStatusAndStartAfter(
                owner.getId(),
                Status.REJECTED,
                LocalDateTime.now().minusDays(30),
                pageable
        );

        assertEquals(1, result.size());
        assertEquals(rejectedBooking.getId(), result.getFirst().getId());
    }
}