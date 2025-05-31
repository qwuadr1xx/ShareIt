package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class ItemDtoIn {
    @NotBlank(message = "Name can't be blank")
    private String name;

    @NotBlank(message = "Description can't be blank")
    private String description;

    @NotNull(message = "Item must contain available value")
    private Boolean available;

    private Long requestId;
}
