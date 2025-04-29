package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class ItemDtoOut {
    private final Long id;

    private final String name;

    private final String description;

    private final Boolean available;

    private final List<CommentDtoOut> comments;

    private final BookingDtoOut lastBooking;

    private final BookingDtoOut nextBooking;
}
