package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor ;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class ItemDtoOutForRequest {
    private Long id;

    private String name;

    private Long ownerId;
}
