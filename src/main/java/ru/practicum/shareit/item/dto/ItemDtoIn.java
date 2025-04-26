package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ItemDtoIn {
    @NotBlank(message = "Name can't be blank")
    private final String name;

    @NotBlank(message = "Description can't be blank")
    private final String description;

    @NotNull(message = "Item must contain available value")
    private final Boolean available;
}
