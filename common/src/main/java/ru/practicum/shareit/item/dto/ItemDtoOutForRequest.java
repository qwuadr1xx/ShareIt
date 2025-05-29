package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ItemDtoOutForRequest {
    private final Long id;

    private final String name;

    private final Long ownerId;
}
