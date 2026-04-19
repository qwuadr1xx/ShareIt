package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoOut save(ItemRequestDtoIn itemRequestDtoIn, Long id);

    List<ItemRequestDtoOut> getUserRequests(Long userId);

    List<ItemRequestDtoOut> getAllRequests(Long userId);

    ItemRequestDtoOut getRequestById(Long requestId);

    ItemRequest findByIdOrThrow(Long requestId);
}
