package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.comment.dto.CommentDtoIn;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class CommentClientTest {
    private CommentClient spyClient;

    @BeforeEach
    void setUp() {
        CommentClient realClient = new CommentClient("http://localhost:8080");
        spyClient = spy(realClient);
    }

    @Test
    void addComment_ShouldUsePostAndReturnDto() {
        CommentDtoIn inDto = CommentDtoIn.builder()
                .text("Nice!")
                .build();
        CommentDtoOut outDto = CommentDtoOut.builder()
                .id(10L)
                .text("Nice!")
                .authorName("Alice")
                .created(LocalDateTime.of(2025, 6, 15, 10, 30, 45))
                .build();

        long userId = 1L;
        long itemId = 5L;
        doReturn(ResponseEntity.ok(outDto))
                .when(spyClient)
                .addComment(itemId, userId, inDto);

        ResponseEntity<CommentDtoOut> response = spyClient.addComment(itemId, userId, inDto);

        assertEquals(10L, response.getBody().getId());
        assertEquals("Nice!", response.getBody().getText());
        assertEquals("Alice", response.getBody().getAuthorName());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
