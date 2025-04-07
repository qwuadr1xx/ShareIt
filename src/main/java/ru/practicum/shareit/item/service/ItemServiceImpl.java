package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto getItemById(long itemId) {
        Item item = itemRepository.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException(itemId, Item.class.toString()));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsByOwner(long userId) {
        userService.getUserById(userId);
        return itemRepository.getItemsByOwner(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.searchItems(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto saveItem(ItemDto itemDto, long userId) {
        userService.getUserById(userId);
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(itemRepository.saveItem(item, userId));
    }

    @Override
    public ItemDto updateItem(long itemId, ItemDto itemDto, long userId) {
        userService.getUserById(userId);
        Item existingItem = itemRepository.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException(itemId, Item.class.toString()));

        if (existingItem.getOwnerId() != userId) {
            throw new NotOwnerException(userId, Item.class.toString());
        }

        Item updatedItem = existingItem.toBuilder()
                .name(itemDto.getName() == null ? existingItem.getName() : itemDto.getName())
                .description(itemDto.getDescription() == null ? existingItem.getDescription() : itemDto.getDescription())
                .available(itemDto.getAvailable() == null ? existingItem.getAvailable() : itemDto.getAvailable())
                .build();

        return ItemMapper.toItemDto(itemRepository.updateItem(updatedItem, userId));
    }
}
