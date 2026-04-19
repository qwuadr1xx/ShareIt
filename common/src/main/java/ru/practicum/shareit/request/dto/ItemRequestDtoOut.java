package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDtoOutForRequest;
import ru.practicum.shareit.user.dto.UserDtoForItemRequest;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemRequestDtoOut {
    private Long id;

    private String description;

    private LocalDateTime created;

    private UserDtoForItemRequest requestor;

    private List<ItemDtoOutForRequest> items;
}
