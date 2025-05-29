package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDtoIn;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoOutShort;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDtoOut> getItem(@PathVariable long itemId, @RequestHeader(HEADER_USER_ID) long userId) {
        log.info("Получен GET-запрос для itemId: {}", itemId);
        return ResponseEntity.ok(itemService.getItemById(itemId, userId));
    }

    @GetMapping
    public ResponseEntity<List<ItemDtoOut>> getItemsByOwner(@RequestHeader(HEADER_USER_ID) long userId) {
        log.info("Получен GET-запрос для списка товаров пользователя с userId: {}", userId);
        return ResponseEntity.ok(itemService.getItemsByOwner(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDtoOutShort>> searchItems(@RequestParam String text) {
        log.info("Получен GET-запрос для поиска товаров по тексту: {}", text);
        return ResponseEntity.ok(itemService.searchItems(text));
    }

    @PostMapping
    public ResponseEntity<ItemDtoOutShort> saveNewItem(@Valid @RequestBody ItemDtoIn itemDto,
                                                       @RequestHeader(HEADER_USER_ID) long userId) {
        log.info("Получен POST-запрос для создания товара с названием: {}", itemDto.getName());
        return ResponseEntity.ok(itemService.saveItem(itemDto, userId));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDtoOutShort> updateItem(@PathVariable long itemId,
                                                      @RequestBody ItemDtoIn itemDto,
                                                      @RequestHeader(HEADER_USER_ID) long userId) {
        log.info("Получен PATCH-запрос для обновления товара с itemId: {}", itemId);
        return ResponseEntity.ok(itemService.updateItem(itemId, itemDto, userId));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDtoOut> addComment(@PathVariable long itemId,
                                    @RequestHeader(HEADER_USER_ID) long userId,
                                    @RequestBody @Valid CommentDtoIn commentDto) {
        log.info("POST: комментарий к itemId={} от userId={}", itemId, userId);
        return ResponseEntity.ok(itemService.addComment(itemId, userId, commentDto));
    }
}
