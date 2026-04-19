package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private User user;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Name")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Need item")
                .created(LocalDateTime.now())
                .requestor(user)
                .build();
    }

    @Test
    void save_shouldCreateRequest() {
        ItemRequestDtoIn dtoIn = ItemRequestDtoIn.builder()
                .description("Need item")
                .build();

        when(userService.findByIdOrThrow(1L)).thenReturn(user);
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDtoOut result = itemRequestService.save(dtoIn, 1L);

        assertNotNull(result);
        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(itemRequest.getDescription(), result.getDescription());
    }

    @Test
    void getUserRequests_shouldReturnRequests() {
        when(itemRequestRepository.findAllRequestsByUserId(1L)).thenReturn(List.of(itemRequest));

        List<ItemRequestDtoOut> result = itemRequestService.getUserRequests(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Need item", result.getFirst().getDescription());
    }

    @Test
    void getAllRequests_shouldReturnOtherUsersRequests() {
        when(itemRequestRepository.findAllOtherUsersRequests(1L)).thenReturn(List.of(itemRequest));

        List<ItemRequestDtoOut> result = itemRequestService.getAllRequests(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getRequestById_shouldReturnRequest() {
        when(itemRequestRepository.findByItemRequestId(1L)).thenReturn(Optional.of(itemRequest));

        ItemRequestDtoOut result = itemRequestService.getRequestById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getRequestById_shouldThrowNotFound_ifRequestMissing() {
        when(itemRequestRepository.findByItemRequestId(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> itemRequestService.getRequestById(1L));

        assertEquals(1L, ex.getId());
    }

    @Test
    void findByIdOrThrow_shouldReturnRequest() {
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(itemRequest));

        ItemRequest result = itemRequestService.findByIdOrThrow(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findByIdOrThrow_shouldThrowNotFound_ifRequestMissing() {
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> itemRequestService.findByIdOrThrow(1L));

        assertEquals(1L, ex.getId());
    }
}
