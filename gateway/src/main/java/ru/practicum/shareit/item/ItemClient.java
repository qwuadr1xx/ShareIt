package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoOutShort;

import java.util.List;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl) {
        super(RestClient.builder().baseUrl(serverUrl + API_PREFIX).build(), serverUrl + API_PREFIX);
    }

    public ResponseEntity<ItemDtoOut> getItemById(long itemId, long userId) {
        return get("/" + itemId, userId, ItemDtoOut.class);
    }

    public ResponseEntity<List<ItemDtoOut>> getItemsByOwner(long userId) {
        return getList("", userId, ItemDtoOut[].class);
    }

    public ResponseEntity<List<ItemDtoOutShort>> searchItems(String text) {
        return getList("/search?text={text}", Map.of("text", text), ItemDtoOutShort[].class);
    }

    public ResponseEntity<ItemDtoOutShort> saveItem(ItemDtoIn itemDto, long userId) {
        return post("", userId, itemDto, ItemDtoOutShort.class);
    }

    public ResponseEntity<ItemDtoOutShort> updateItem(long itemId, ItemDtoIn itemDto, long userId) {
        return patch("/" + itemId, userId, itemDto, ItemDtoOutShort.class);
    }
}
