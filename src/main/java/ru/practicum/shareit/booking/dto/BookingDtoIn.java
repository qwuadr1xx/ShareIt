package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class BookingDtoIn {
    @Positive(message = "Item id must be positive")
    private final Long itemId;

    private final LocalDateTime start;

    private final LocalDateTime end;
}
