package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.comment.dto.CommentDtoIn;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;

@Service
public class CommentClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public CommentClient(@Value("${shareit-server.url}") String serverUrl) {
        super(RestClient.builder().baseUrl(serverUrl + API_PREFIX).build(), serverUrl + API_PREFIX);
    }

    public ResponseEntity<CommentDtoOut> addComment(long itemId, long userId, CommentDtoIn dto) {
        return post("/" + itemId + "/comment", userId, dto, CommentDtoOut.class);
    }
}
