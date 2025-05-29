package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentDtoIn;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoOutShort;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@ContextConfiguration(classes = ShareItServer.class)
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    private final ItemDtoIn itemDtoIn = ItemDtoIn.builder()
            .name("Name")
            .description("Description")
            .available(true)
            .build();

    private final ItemDtoOut itemDtoOut = ItemDtoOut.builder()
            .id(1L)
            .name("Name")
            .description("Description")
            .available(true)
            .build();

    private final ItemDtoOutShort itemDtoOutShort = ItemDtoOutShort.builder()
            .id(1L)
            .name("Name")
            .description("Description")
            .available(true)
            .build();

    private final CommentDtoIn commentDtoIn = CommentDtoIn.builder()
            .text("Comment")
            .build();

    private final CommentDtoOut commentDtoOut = CommentDtoOut.builder()
            .id(1L)
            .text("Comment")
            .authorName("Author")
            .build();

    @Test
    void saveNewItem_shouldReturnOk() throws Exception {
        when(itemService.saveItem(any(), anyLong())).thenReturn(itemDtoOutShort);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Name"));
    }

    @Test
    void getItem_shouldReturnOk() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(itemDtoOut);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Description"));
    }

    @Test
    void getItemsByOwner_shouldReturnOk() throws Exception {
        when(itemService.getItemsByOwner(anyLong())).thenReturn(List.of(itemDtoOut));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Name"));
    }

    @Test
    void searchItems_shouldReturnOk() throws Exception {
        when(itemService.searchItems(any())).thenReturn(List.of(itemDtoOutShort));

        mockMvc.perform(get("/items/search")
                        .param("text", "query"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Name"));
    }

    @Test
    void updateItem_shouldReturnOk() throws Exception {
        when(itemService.updateItem(anyLong(), any(), anyLong())).thenReturn(itemDtoOutShort);

        mockMvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Description"));
    }

    @Test
    void addComment_shouldReturnOk() throws Exception {
        when(itemService.addComment(anyLong(), anyLong(), any())).thenReturn(commentDtoOut);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Comment"));
    }

    @Test
    void getItemById_whenItemNotFound_thenNotFoundException() throws Exception {
        Long itemId = 999L;
        Long userId = 1L;

        when(itemService.getItemById(itemId, userId))
                .thenThrow(new NotFoundException(itemId, "Item"));

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Ошибка с введенным id"))
                .andExpect(jsonPath("$.description").value("id 999 сущности Item не находится в базе"));
    }
}
