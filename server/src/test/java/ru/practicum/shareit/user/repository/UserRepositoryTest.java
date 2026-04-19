package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = ShareItServer.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void saveUser_ShouldPersistAndAssignId() {
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .build();

        User saved = userRepository.save(user);

        assertNotNull(saved.getId(), "ID должен быть присвоен после сохранения");
        assertEquals("Test User", saved.getName());
        assertEquals("test@example.com", saved.getEmail());
        User fetched = userRepository.findById(saved.getId()).orElseThrow();
        assertEquals(saved.getId(), fetched.getId());
        assertEquals("Test User", fetched.getName());
    }
}
