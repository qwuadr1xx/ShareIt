package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ItemDtoOutShort {
    private final Long id;

    private final String name;

    private final String description;

    private final Boolean available;
}
