package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDtoIn;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoOutShort;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDtoOut getItemById(Long itemId, Long userId);

    List<ItemDtoOut> getItemsByOwner(Long userId);

    List<ItemDtoOutShort> searchItems(String text);

    ItemDtoOutShort saveItem(ItemDtoIn itemDto, Long userId);

    ItemDtoOutShort updateItem(Long itemId, ItemDtoIn itemDto, Long userId);

    CommentDtoOut addComment(Long itemId, Long userId, CommentDtoIn commentDto);

    Item findByIdOrThrow(Long id);
}
