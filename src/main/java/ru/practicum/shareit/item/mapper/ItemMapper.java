package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoOutShort;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemMapper {
    public static ItemDtoOut toItemDto(Item item, List<CommentDtoOut> commentDtoOuts, BookingDtoOut lastBooking, BookingDtoOut nextBooking) {
        return ItemDtoOut.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(commentDtoOuts)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .build();
    }

    public static Item toItem(ItemDtoIn itemDtoIn) {
        return Item.builder()
                .name(itemDtoIn.getName())
                .description(itemDtoIn.getDescription())
                .available(itemDtoIn.getAvailable())
                .build();
    }

    public static ItemDtoOutShort toItemDtoShort(Item item) {
        return ItemDtoOutShort.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }
}