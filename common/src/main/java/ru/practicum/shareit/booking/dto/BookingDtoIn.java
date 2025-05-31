package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class BookingDtoIn {
    @Positive(message = "Item id must be positive")
    private Long itemId;

    private LocalDateTime start;

    private LocalDateTime end;
}
