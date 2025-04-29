package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.dto.CommentDtoIn;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoOutShort;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional(readOnly = true)
    public ItemDtoOut getItemById(Long itemId, Long userId) {
        Item item = findByIdOrThrow(itemId);

        if (!item.getOwner().getId().equals(userId)) {
            return ItemMapper.toItemDto(
                    item,
                    commentRepository.findByItemId(itemId).stream()
                            .map(CommentMapper::toCommentDto).collect(Collectors.toList()),
                    null,
                    null
            );
        }

        return ItemMapper.toItemDto(
                item,
                commentRepository.findByItemId(itemId).stream()
                        .map(CommentMapper::toCommentDto).collect(Collectors.toList()),
                BookingMapper.toBookingDto(bookingRepository.findLastBookingByItemIdOrderByStartDesc(itemId)),
                BookingMapper.toBookingDto(bookingRepository.findFirstBookingByItemIdOrderByStartAsc(itemId))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDtoOut> getItemsByOwner(Long userId) {
        userService.getUserById(userId);

        return itemRepository.findByOwnerId(userId)
                .stream()
                .map(item -> {
                    Long itemId = item.getId();

                    List<CommentDtoOut> comments = commentRepository.findByItemId(itemId)
                            .stream()
                            .map(CommentMapper::toCommentDto)
                            .collect(Collectors.toList());

                    BookingDtoOut lastBooking = BookingMapper.toBookingDto(
                            bookingRepository.findLastBookingByItemIdOrderByStartDesc(itemId)
                    );

                    BookingDtoOut nextBooking = BookingMapper.toBookingDto(
                            bookingRepository.findFirstBookingByItemIdOrderByStartAsc(itemId)
                    );

                    return ItemMapper.toItemDto(item, comments, lastBooking, nextBooking);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDtoOutShort> searchItems(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.searchAvailableItems(text)
                .stream()
                .map(ItemMapper::toItemDtoShort)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemDtoOutShort saveItem(ItemDtoIn itemDtoIn, Long userId) {
        User user;
        Item item = ItemMapper.toItem(itemDtoIn);
        try {
            user = userService.findByIdOrThrow(userId);
        } catch (RuntimeException e) {
            throw new NotFoundException(userId, "Item");
        }
        item.setOwner(user);

        return ItemMapper.toItemDtoShort(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDtoOutShort updateItem(Long itemId, ItemDtoIn itemDtoIn, Long userId) {
        userService.findByIdOrThrow(userId);
        Item existingItem = findByIdOrThrow(itemId);

        if (!existingItem.getOwner().getId().equals(userId)) {
            throw new NotOwnerException(userId, Item.class.toString());
        }

        Item updatedItem = existingItem.toBuilder()
                .name(itemDtoIn.getName() == null ? existingItem.getName() : itemDtoIn.getName())
                .description(itemDtoIn.getDescription() == null ? existingItem.getDescription() : itemDtoIn.getDescription())
                .available(itemDtoIn.getAvailable() == null ? existingItem.getAvailable() : itemDtoIn.getAvailable())
                .build();

        return ItemMapper.toItemDtoShort(itemRepository.save(updatedItem));
    }

    @Override
    @Transactional
    public CommentDtoOut addComment(Long itemId, Long userId, CommentDtoIn commentDto) {
        Item item = findByIdOrThrow(itemId);
        User user = userService.findByIdOrThrow(userId);

        boolean hasCompletedBooking = bookingRepository
                .findPastByUser(userId)
                .stream()
                .anyMatch(booking -> booking.getItem().getId().equals(itemId));

        if (!hasCompletedBooking) {
            throw new BadRequestException("Пользователь не бронировал этот предмет", "Comment");
        }

        Comment savedComment = commentRepository.save(Comment.builder()
                .text(commentDto.getText())
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .build());

        return CommentMapper.toCommentDto(savedComment);
    }

    public Item findByIdOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(itemId, "Item"));
    }
}