package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.enums.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b JOIN b.item i WHERE b.id = :bookingId AND i.owner.id = :ownerId")
    Optional<Booking> findByIdAndItemOwnerId(
            @Param("bookingId") Long bookingId,
            @Param("ownerId") Long ownerId
    );

    @Query("""
        SELECT b
        FROM Booking b
        JOIN b.item i
        WHERE b.id = :bookingId
          AND (b.booker.id = :userId OR i.owner.id = :userId)
        """)
    Optional<Booking> findAccessibleBooking(
            @Param("bookingId") Long bookingId,
            @Param("userId") Long userId
    );

    @Query("""
        SELECT b
        FROM Booking b
        WHERE b.booker.id = :userId
          AND b.start >= :from
        ORDER BY b.start DESC
        """)
    List<Booking> findByUserAndStartAfter(
            @Param("userId") Long userId,
            @Param("from") LocalDateTime from,
            Pageable pageable
    );

    @Query("""
        SELECT b
        FROM Booking b
        WHERE b.booker.id = :userId
          AND b.start <= CURRENT_TIMESTAMP
          AND b.start >= :from
        ORDER BY b.start DESC
        """)
    List<Booking> findCurrentByUserAndStartAfter(
            @Param("userId") Long userId,
            @Param("from") LocalDateTime from,
            Pageable pageable
    );

    @Query("""
        SELECT b
        FROM Booking b
        WHERE b.booker.id = :userId
          AND b.end < CURRENT_TIMESTAMP
          AND b.start >= :from
        ORDER BY b.start DESC
        """)
    List<Booking> findPastByUserAndStartAfter(
            @Param("userId") Long userId,
            @Param("from") LocalDateTime from,
            Pageable pageable
    );

    @Query("""
        SELECT b
        FROM Booking b
        WHERE b.booker.id = :userId
          AND b.start > CURRENT_TIMESTAMP
          AND b.start >= :from
        ORDER BY b.start DESC
        """)
    List<Booking> findFutureByUserAndStartAfter(
            @Param("userId") Long userId,
            @Param("from") LocalDateTime from,
            Pageable pageable
    );

    @Query("""
        SELECT b
        FROM Booking b
        WHERE b.booker.id = :userId
          AND b.status = :status
          AND b.start >= :from
        ORDER BY b.start DESC
        """)
    List<Booking> findUserBookingsByStatusAndStartAfter(
            @Param("userId") Long userId,
            @Param("status") Status status,
            @Param("from") LocalDateTime from,
            Pageable pageable
    );

    // --- аналогично для owner ---
    @Query("""
        SELECT b
        FROM Booking b
        JOIN b.item i
        WHERE i.owner.id = :ownerId
          AND b.start >= :from
        ORDER BY b.start DESC
        """)
    List<Booking> findByOwnerAndStartAfter(
            @Param("ownerId") Long ownerId,
            @Param("from") LocalDateTime from,
            Pageable pageable
    );

    @Query("""
        SELECT b
        FROM Booking b
        JOIN b.item i
        WHERE i.owner.id = :ownerId
          AND b.start <= CURRENT_TIMESTAMP
          AND b.start >= :from
        ORDER BY b.start DESC
        """)
    List<Booking> findCurrentByOwnerAndStartAfter(
            @Param("ownerId") Long ownerId,
            @Param("from") LocalDateTime from,
            Pageable pageable
    );

    @Query("""
        SELECT b
        FROM Booking b
        JOIN b.item i
        WHERE i.owner.id = :ownerId
          AND b.end < CURRENT_TIMESTAMP
          AND b.start >= :from
        ORDER BY b.start DESC
        """)
    List<Booking> findPastByOwnerAndStartAfter(
            @Param("ownerId") Long ownerId,
            @Param("from") LocalDateTime from,
            Pageable pageable
    );

    @Query("""
        SELECT b
        FROM Booking b
        JOIN b.item i
        WHERE i.owner.id = :ownerId
          AND b.start > CURRENT_TIMESTAMP
          AND b.start >= :from
        ORDER BY b.start DESC
        """)
    List<Booking> findFutureByOwnerAndStartAfter(
            @Param("ownerId") Long ownerId,
            @Param("from") LocalDateTime from,
            Pageable pageable
    );

    @Query("""
        SELECT b
        FROM Booking b
        JOIN b.item i
        WHERE i.owner.id = :ownerId
          AND b.status = :status
          AND b.start >= :from
        ORDER BY b.start DESC
        """)
    List<Booking> findOwnerBookingsByStatusAndStartAfter(
            @Param("ownerId") Long ownerId,
            @Param("status") Status status,
            @Param("from") LocalDateTime from,
            Pageable pageable
    );

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId ORDER BY b.start ASC")
    Booking findFirstBookingByItemIdOrderByStartAsc(@Param("itemId") Long itemId);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId ORDER BY b.start DESC")
    Booking findLastBookingByItemIdOrderByStartDesc(@Param("itemId") Long itemId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.end < CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<Booking> findPastByUser(@Param("userId") Long userId);
}
