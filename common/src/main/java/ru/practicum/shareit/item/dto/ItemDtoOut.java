package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor ;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;

import java.util.List;

@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class ItemDtoOut {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private List<CommentDtoOut> comments;

    private BookingDtoOut lastBooking;

    private BookingDtoOut nextBooking;
}
