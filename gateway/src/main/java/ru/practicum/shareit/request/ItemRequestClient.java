package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.List;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl) {
        super(RestClient.builder().baseUrl(serverUrl + API_PREFIX).build(), serverUrl + API_PREFIX);
    }

    public ResponseEntity<ItemRequestDtoOut> save(ItemRequestDtoIn dto, long userId) {
        return post("", userId, dto, ItemRequestDtoOut.class);
    }

    public ResponseEntity<List<ItemRequestDtoOut>> getUserRequests(long userId) {
        return getList("", userId, ItemRequestDtoOut[].class);
    }

    public ResponseEntity<List<ItemRequestDtoOut>> getAllRequests(long userId) {
        return getList("/all", userId, ItemRequestDtoOut[].class);
    }

    public ResponseEntity<ItemRequestDtoOut> getRequestById(long requestId) {
        return get("/" + requestId, ItemRequestDtoOut.class);
    }
}
