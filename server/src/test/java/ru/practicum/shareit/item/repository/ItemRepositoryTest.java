package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = ShareItServer.class)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User owner;
    private Item drill;
    private Item hammer;

    @BeforeEach
    void setUp() {
        // Очистка
        itemRepository.deleteAll();
        userRepository.deleteAll();

        owner = userRepository.save(User.builder()
                .name("Owner")
                .email("owner@example.com")
                .build());

        drill = itemRepository.save(Item.builder()
                .name("Drill")
                .description("Electric drill")
                .available(true)
                .owner(owner)
                .build());

        hammer = itemRepository.save(Item.builder()
                .name("Hammer")
                .description("Steel hammer")
                .available(true)
                .owner(owner)
                .build());

        Item saw = itemRepository.save(Item.builder()
                .name("Saw")
                .description("Hand saw")
                .available(false)
                .owner(owner)
                .build());
    }

    @Test
    void findByOwnerId_ShouldReturnAllOwnedItems() {
        List<Item> items = itemRepository.findByOwnerId(owner.getId());
        assertNotNull(items);
        assertEquals(3, items.size());
        assertTrue(items.stream().allMatch(i -> i.getOwner().getId().equals(owner.getId())));
    }

    @Test
    void searchAvailableItems_ShouldMatchByNameOrDescription_CaseInsensitive() {
        List<Item> nameSearch = itemRepository.searchAvailableItems("dRiLL");
        assertEquals(1, nameSearch.size());
        assertEquals(drill.getId(), nameSearch.getFirst().getId());

        List<Item> descSearch = itemRepository.searchAvailableItems("steel");
        assertEquals(1, descSearch.size());
        assertEquals(hammer.getId(), descSearch.getFirst().getId());
    }

    @Test
    void searchAvailableItems_ShouldNotReturnUnavailable() {
        List<Item> result = itemRepository.searchAvailableItems("saw");
        assertTrue(result.isEmpty());
    }

    @Test
    void searchAvailableItems_WithNoMatches_ShouldReturnEmptyList() {
        List<Item> result = itemRepository.searchAvailableItems("xyz");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
