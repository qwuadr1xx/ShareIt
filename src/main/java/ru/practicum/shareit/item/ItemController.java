package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable long itemId) {
        log.info("Получен GET-запрос для itemId: {}", itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getItemsByOwner(@RequestHeader(HEADER_USER_ID) long userId) {
        log.info("Получен GET-запрос для списка товаров пользователя с userId: {}", userId);
        return itemService.getItemsByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> SearchItems(@RequestParam String text) {
        log.info("Получен GET-запрос для поиска товаров по тексту: {}", text);
        return itemService.SearchItems(text);
    }

    @PostMapping
    public ItemDto saveNewItem(@Valid @RequestBody ItemDto itemDto,
                               @RequestHeader(HEADER_USER_ID) long userId) {
        log.info("Получен POST-запрос для создания товара с названием: {}", itemDto.getName());
        return itemService.saveItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable long itemId,
                              @RequestBody ItemDto itemDto,
                              @RequestHeader(HEADER_USER_ID) long userId) {
        log.info("Получен PATCH-запрос для обновления товара с itemId: {}", itemId);
        return itemService.updateItem(itemId, itemDto, userId);
    }
}
