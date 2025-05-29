package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class ItemRequestClientTest {
    private ItemRequestClient spyClient;
    private final long userId = 1L;
    private final long requestId = 99L;

    @BeforeEach
    void setUp() {
        // создаём реальный клиент и оборачиваем в spy
        ItemRequestClient realClient = new ItemRequestClient("http://localhost:8080");
        spyClient = spy(realClient);
    }

    @Test
    void save_ShouldPostAndReturnDto() {
        ItemRequestDtoIn inDto = ItemRequestDtoIn.builder()
                .description("Need a drill")
                .build();
        ItemRequestDtoOut outDto = ItemRequestDtoOut.builder()
                .id(requestId)
                .description("Need a drill")
                .created(LocalDateTime.of(2025, 7, 20, 14, 0))
                .requestor(ru.practicum.shareit.user.dto.UserDtoForItemRequest.builder()
                        .id(userId).name("Bob").build())
                .items(List.of())
                .build();

        doReturn(ResponseEntity.ok(outDto))
                .when(spyClient).save(inDto, userId);

        ResponseEntity<ItemRequestDtoOut> resp = spyClient.save(inDto, userId);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(requestId, resp.getBody().getId());
        assertEquals("Need a drill", resp.getBody().getDescription());
    }

    @Test
    void getUserRequests_ShouldReturnList() {
        ItemRequestDtoOut element = ItemRequestDtoOut.builder()
                .id(1L).description("Req1").created(LocalDateTime.now())
                .requestor(null).items(List.of()).build();
        List<ItemRequestDtoOut> stub = List.of(element);

        doReturn(ResponseEntity.ok(stub))
                .when(spyClient).getUserRequests(userId);

        ResponseEntity<List<ItemRequestDtoOut>> resp = spyClient.getUserRequests(userId);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1, resp.getBody().size());
        assertEquals(1L, resp.getBody().getFirst().getId());
    }

    @Test
    void getAllRequests_ShouldReturnList() {
        ItemRequestDtoOut element = ItemRequestDtoOut.builder()
                .id(2L).description("Req2").created(LocalDateTime.now())
                .requestor(null).items(List.of()).build();
        List<ItemRequestDtoOut> stub = List.of(element);

        doReturn(ResponseEntity.ok(stub))
                .when(spyClient).getAllRequests(userId);

        ResponseEntity<List<ItemRequestDtoOut>> resp = spyClient.getAllRequests(userId);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1, resp.getBody().size());
        assertEquals(2L, resp.getBody().getFirst().getId());
    }

    @Test
    void getRequestById_ShouldReturnDto() {
        ItemRequestDtoOut outDto = ItemRequestDtoOut.builder()
                .id(requestId).description("SingleReq")
                .created(LocalDateTime.now()).requestor(null).items(List.of())
                .build();

        doReturn(ResponseEntity.ok(outDto))
                .when(spyClient).getRequestById(requestId);

        ResponseEntity<ItemRequestDtoOut> resp = spyClient.getRequestById(requestId);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(requestId, resp.getBody().getId());
        assertEquals("SingleReq", resp.getBody().getDescription());
    }
}
