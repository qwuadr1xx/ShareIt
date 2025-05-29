package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.dto.ItemDtoOutShort;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class BookingDtoOut {
    private final Long id;

    private final LocalDateTime start;

    private final LocalDateTime end;

    private final ItemDtoOutShort item;

    private final UserDto booker;

    private final Status status;
}
