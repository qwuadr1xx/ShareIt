package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class ItemRequestMapper {
    public static ItemRequest toItemRequest(ItemRequestDtoIn itemRequestDtoIn, User user) {
        return ItemRequest.builder()
                .description(itemRequestDtoIn.getDescription())
                .created(LocalDateTime.now())
                .requestor(user)
                .build();
    }

    public static ItemRequestDtoOut toItemRequestDtoOut(ItemRequest itemRequest) {
        return ItemRequestDtoOut.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .requestor(UserMapper.toUserDtoForItemRequest(itemRequest.getRequestor()))
                .items(itemRequest.getItems() == null ? null : itemRequest.getItems().stream().map(ItemMapper::toItemDtoOutForRequest).toList())
                .build();
    }
}