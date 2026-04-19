package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoOutShort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class ItemClientTest {
    private ItemClient spyClient;
    private final long userId = 1L;
    private final long itemId = 10L;

    @BeforeEach
    void setUp() {
        ItemClient realClient = new ItemClient("http://localhost:8080");
        spyClient = spy(realClient);
    }

    @Test
    void getItemById_ShouldReturnItemDtoOut() {
        ItemDtoOut outDto = ItemDtoOut.builder().id(itemId).build();

        doReturn(ResponseEntity.ok(outDto))
                .when(spyClient).getItemById(itemId, userId);

        ResponseEntity<ItemDtoOut> response = spyClient.getItemById(itemId, userId);

        assertEquals(itemId, response.getBody().getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getItemsByOwner_ShouldReturnList() {
        ItemDtoOut item = ItemDtoOut.builder().id(itemId).build();
        List<ItemDtoOut> stubList = List.of(item);

        doReturn(ResponseEntity.ok(stubList))
                .when(spyClient).getItemsByOwner(userId);

        ResponseEntity<List<ItemDtoOut>> response = spyClient.getItemsByOwner(userId);

        assertEquals(1, response.getBody().size());
        assertEquals(itemId, response.getBody().getFirst().getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void searchItems_ShouldReturnShortList() {
        ItemDtoOutShort item = ItemDtoOutShort.builder().id(itemId).build();
        List<ItemDtoOutShort> stubList = List.of(item);

        doReturn(ResponseEntity.ok(stubList))
                .when(spyClient).searchItems("text");

        ResponseEntity<List<ItemDtoOutShort>> response = spyClient.searchItems("text");

        assertEquals(1, response.getBody().size());
        assertEquals(itemId, response.getBody().getFirst().getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void saveItem_ShouldReturnShortDto() {
        ItemDtoIn inDto = ItemDtoIn.builder().name("item").build();
        ItemDtoOutShort outDto = ItemDtoOutShort.builder().id(itemId).build();

        doReturn(ResponseEntity.ok(outDto))
                .when(spyClient).saveItem(inDto, userId);

        ResponseEntity<ItemDtoOutShort> response = spyClient.saveItem(inDto, userId);

        assertEquals(itemId, response.getBody().getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateItem_ShouldReturnShortDto() {
        ItemDtoIn inDto = ItemDtoIn.builder().name("updated").build();
        ItemDtoOutShort outDto = ItemDtoOutShort.builder().id(itemId).build();

        doReturn(ResponseEntity.ok(outDto))
                .when(spyClient).updateItem(itemId, inDto, userId);

        ResponseEntity<ItemDtoOutShort> response = spyClient.updateItem(itemId, inDto, userId);

        assertEquals(itemId, response.getBody().getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
