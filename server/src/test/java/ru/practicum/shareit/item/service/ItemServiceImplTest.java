package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.dto.CommentDtoIn;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoOutShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemServiceImpl itemService;

    private User user;
    private Item item;
    private ItemDtoIn itemDtoIn;
    private ItemRequest request;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Owner")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Item")
                .description("Description")
                .available(true)
                .owner(user)
                .build();

        itemDtoIn = ItemDtoIn.builder()
                .name("Item")
                .description("Description")
                .available(true)
                .build();

        request = ItemRequest.builder()
                .id(10L)
                .build();
    }

    @Test
    void saveItem_shouldSaveItemWithRequest() {
        itemDtoIn.setRequestId(10L);

        when(userService.findByIdOrThrow(1L)).thenReturn(user);
        when(itemRequestService.findByIdOrThrow(10L)).thenReturn(request);
        when(itemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ItemDtoOutShort result = itemService.saveItem(itemDtoIn, 1L);

        assertNotNull(result);
        assertEquals("Item", result.getName());
    }

    @Test
    void getItemById_shouldReturnItemForOwner() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(1L)).thenReturn(List.of());
        when(bookingRepository.findLastBookingByItemIdOrderByStartDesc(1L)).thenReturn(null);
        when(bookingRepository.findFirstBookingByItemIdOrderByStartAsc(1L)).thenReturn(null);

        ItemDtoOut result = itemService.getItemById(1L, 1L);

        assertNotNull(result);
        assertEquals("Item", result.getName());
    }

    @Test
    void getItemById_shouldReturnItemForAnotherUserWithoutBookings() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(1L)).thenReturn(List.of());

        ItemDtoOut result = itemService.getItemById(1L, 99L);

        assertNotNull(result);
        assertNull(result.getLastBooking());
        assertNull(result.getNextBooking());
    }

    @Test
    void getItemsByOwner_shouldReturnItemsList() {
        when(itemRepository.findByOwnerId(1L)).thenReturn(List.of(item));
        when(commentRepository.findByItemId(1L)).thenReturn(List.of());
        when(bookingRepository.findLastBookingByItemIdOrderByStartDesc(1L)).thenReturn(null);
        when(bookingRepository.findFirstBookingByItemIdOrderByStartAsc(1L)).thenReturn(null);

        List<ItemDtoOut> result = itemService.getItemsByOwner(1L);

        assertEquals(1, result.size());
    }

    @Test
    void searchItems_shouldReturnEmptyList_whenTextIsBlank() {
        List<ItemDtoOutShort> result = itemService.searchItems(" ");
        assertTrue(result.isEmpty());
    }

    @Test
    void updateItem_shouldUpdateAllFields() {
        when(userService.findByIdOrThrow(1L)).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ItemDtoOutShort result = itemService.updateItem(1L, itemDtoIn, 1L);

        assertEquals("Item", result.getName());
    }

    @Test
    void updateItem_shouldThrowNotOwnerException() {
        when(userService.findByIdOrThrow(2L)).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(NotOwnerException.class, () -> itemService.updateItem(1L, itemDtoIn, 2L));
    }

    @Test
    void addComment_shouldCreateComment() {
        CommentDtoIn commentDto = CommentDtoIn.builder().text("Great!").build();
        Comment savedComment = Comment.builder().id(1L).text("Great!").item(item).author(user).created(LocalDateTime.now()).build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userService.findByIdOrThrow(1L)).thenReturn(user);
        when(bookingRepository.findPastByUser(1L)).thenReturn(List.of(
                Booking.builder().item(item).build()
        ));
        when(commentRepository.save(any())).thenReturn(savedComment);

        var result = itemService.addComment(1L, 1L, commentDto);

        assertNotNull(result);
        assertEquals("Great!", result.getText());
    }

    @Test
    void addComment_shouldThrowBadRequest_whenNoPastBooking() {
        CommentDtoIn commentDto = CommentDtoIn.builder().text("Great!").build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userService.findByIdOrThrow(1L)).thenReturn(user);
        when(bookingRepository.findPastByUser(1L)).thenReturn(List.of());

        assertThrows(BadRequestException.class, () -> itemService.addComment(1L, 1L, commentDto));
    }

    @Test
    void findByIdOrThrow_shouldThrowNotFound_whenItemMissing() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.findByIdOrThrow(1L));
    }
}
