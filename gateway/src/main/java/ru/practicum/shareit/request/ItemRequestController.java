package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<ItemRequestDtoOut> saveNewRequest(@Valid @RequestBody ItemRequestDtoIn itemRequestDtoIn,
                                                            @RequestHeader(name = HEADER_USER_ID) Long userId) {
        log.info("POST /requests — создание нового запроса вещи: {}", itemRequestDtoIn);
        return itemRequestClient.save(itemRequestDtoIn, userId);
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDtoOut>> getUserRequests(@RequestHeader(name = HEADER_USER_ID) Long userId) {
        log.info("GET /requests — получение запросов пользователя с ID {}", userId);
        return itemRequestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDtoOut>> getAllRequests(@RequestHeader(name = HEADER_USER_ID) Long userId) {
        log.info("GET /requests/all — получение всех запросов, пользователь ID {}", userId);
        return itemRequestClient.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDtoOut> getRequestById(@PathVariable Long requestId) {
        log.info("GET /requests/{} — получение запроса по ID", requestId);
        return itemRequestClient.getRequestById(requestId);
    }
}
