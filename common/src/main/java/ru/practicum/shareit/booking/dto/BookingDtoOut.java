package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.dto.ItemDtoOutShort;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class BookingDtoOut {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemDtoOutShort item;

    private UserDto booker;

    private Status status;
}
