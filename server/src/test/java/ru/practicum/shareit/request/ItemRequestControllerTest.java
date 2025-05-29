package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
@ContextConfiguration(classes = ShareItServer.class)
public class ItemRequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ItemRequestService itemRequestService;

    private static final String BASE_PATH = "/requests";
    private static final Long USER_ID = 1L;
    private static final Long REQUEST_ID = 999L;

    private final ItemRequestDtoIn itemRequestDtoIn = ItemRequestDtoIn.builder()
            .description("Description")
            .build();

    private final ItemRequestDtoOut itemRequestDtoOut = ItemRequestDtoOut.builder()
            .id(REQUEST_ID)
            .description("Description")
            .build();

    @Test
    void saveNewRequest_shouldCreateRequest() throws Exception {
        when(itemRequestService.save(itemRequestDtoIn, USER_ID))
                .thenReturn(itemRequestDtoOut);

        mockMvc.perform(post(BASE_PATH)
                        .header("X-Sharer-User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(itemRequestDtoIn)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(REQUEST_ID))
                .andExpect(jsonPath("$.description").value("Description"));
    }

    @Test
    void getUserRequests_shouldReturnListOfRequests() throws Exception {
        List<ItemRequestDtoOut> requests = Collections.singletonList(itemRequestDtoOut);
        when(itemRequestService.getUserRequests(USER_ID)).thenReturn(requests);

        mockMvc.perform(get(BASE_PATH)
                        .header("X-Sharer-User-Id", USER_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(REQUEST_ID))
                .andExpect(jsonPath("$[0].description").value("Description"));
    }

    @Test
    void getAllRequests_shouldReturnListOfRequests() throws Exception {
        List<ItemRequestDtoOut> requests = Collections.singletonList(itemRequestDtoOut);
        when(itemRequestService.getAllRequests(USER_ID)).thenReturn(requests);

        mockMvc.perform(get(BASE_PATH + "/all")
                        .header("X-Sharer-User-Id", USER_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(REQUEST_ID))
                .andExpect(jsonPath("$[0].description").value("Description"));
    }

    @Test
    void getRequestById_shouldReturnRequest() throws Exception {
        when(itemRequestService.getRequestById(REQUEST_ID)).thenReturn(itemRequestDtoOut);

        mockMvc.perform(get(BASE_PATH + "/{requestId}", REQUEST_ID)
                        .header("X-Sharer-User-Id", USER_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(REQUEST_ID))
                .andExpect(jsonPath("$.description").value("Description"));
    }

    @Test
    void getRequestById_whenRequestNotFound_thenNotFoundException() throws Exception {
        when(itemRequestService.getRequestById(REQUEST_ID))
                .thenThrow(new NotFoundException(REQUEST_ID, "ItemRequest"));

        mockMvc.perform(get(BASE_PATH + "/{requestId}", REQUEST_ID)
                        .header("X-Sharer-User-Id", USER_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Ошибка с введенным id"))
                .andExpect(jsonPath("$.description").value("id 999 сущности ItemRequest не находится в базе"));
    }
}
