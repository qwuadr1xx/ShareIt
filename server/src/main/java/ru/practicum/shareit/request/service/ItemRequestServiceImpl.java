package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    @Override
    @Transactional
    public ItemRequestDtoOut save(ItemRequestDtoIn itemRequestDtoIn, Long id) {
        User user = userService.findByIdOrThrow(id);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDtoIn, user);

        return ItemRequestMapper.toItemRequestDtoOut(itemRequestRepository.save(itemRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDtoOut> getUserRequests(Long userId) {
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllRequestsByUserId(userId);

        return itemRequestList.stream().map(ItemRequestMapper::toItemRequestDtoOut).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDtoOut> getAllRequests(Long userId) {
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllOtherUsersRequests(userId);

        return itemRequestList.stream().map(ItemRequestMapper::toItemRequestDtoOut).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDtoOut getRequestById(Long requestId) {
        return ItemRequestMapper.toItemRequestDtoOut(itemRequestRepository.findByItemRequestId(requestId)
                .orElseThrow(() -> new NotFoundException(requestId, "ItemRequest")));
    }

    public ItemRequest findByIdOrThrow(Long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(requestId, "itemRequest"));
    }
}
