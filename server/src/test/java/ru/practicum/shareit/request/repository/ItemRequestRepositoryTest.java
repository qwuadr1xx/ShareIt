package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = ShareItServer.class)
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private User userA;
    private ItemRequest requestA1;
    private ItemRequest requestA2;
    private ItemRequest requestB1;

    @BeforeEach
    void setUp() {
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();

        userA = userRepository.save(User.builder()
                .name("Alice")
                .email("alice@example.com")
                .build());

        User userB = userRepository.save(User.builder()
                .name("Bob")
                .email("bob@example.com")
                .build());

        requestA1 = itemRequestRepository.save(ItemRequest.builder()
                .description("Need hammer")
                .created(LocalDateTime.now().minusHours(2))
                .requestor(userA)
                .build());

        requestA2 = itemRequestRepository.save(ItemRequest.builder()
                .description("Looking for drill")
                .created(LocalDateTime.now().minusHours(1))
                .requestor(userA)
                .build());

        requestB1 = itemRequestRepository.save(ItemRequest.builder()
                .description("Want saw")
                .created(LocalDateTime.now().minusHours(3))
                .requestor(userB)
                .build());
    }

    @Test
    void findAllRequestsByUserId_ShouldReturnOnlyThatUserRequestsInDescOrder() {
        List<ItemRequest> results = itemRequestRepository.findAllRequestsByUserId(userA.getId());

        assertEquals(2, results.size());
        assertEquals(requestA2.getId(), results.get(0).getId());
        assertEquals(requestA1.getId(), results.get(1).getId());
    }

    @Test
    void findAllOtherUsersRequests_ShouldReturnRequestsNotByThatUser() {
        List<ItemRequest> results = itemRequestRepository.findAllOtherUsersRequests(userA.getId());

        assertEquals(1, results.size());
        assertEquals(requestB1.getId(), results.getFirst().getId());
    }

    @Test
    void findByItemRequestId_ShouldReturnCorrectRequest() {
        Optional<ItemRequest> foundA1 = itemRequestRepository.findByItemRequestId(requestA1.getId());
        assertTrue(foundA1.isPresent());
        assertEquals("Need hammer", foundA1.get().getDescription());

        Optional<ItemRequest> notFound = itemRequestRepository.findByItemRequestId(999L);
        assertTrue(notFound.isEmpty());
    }
}
