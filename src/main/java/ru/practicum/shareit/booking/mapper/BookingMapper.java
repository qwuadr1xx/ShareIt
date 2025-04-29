package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

public class BookingMapper {
    public static BookingDtoOut toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingDtoOut.builder()
                .id(booking.getId())
                .end(booking.getEnd())
                .start(booking.getStart())
                .status(booking.getStatus())
                .booker(UserMapper.toUserDto(booking.getBooker()))
                .item(ItemMapper.toItemDtoShort(booking.getItem()))
                .build();
    }

    public static Booking toBooking(BookingDtoIn bookingDtoIn) {
        return Booking.builder()
                .end(bookingDtoIn.getEnd())
                .start(bookingDtoIn.getStart())
                .build();
    }
}