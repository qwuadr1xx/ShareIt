package ru.practicum.shareit.item.comment.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = ShareItServer.class)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private Item item1;
    private Item item2;

    private Comment commentB1;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();

        User author = userRepository.save(User.builder()
                .name("Author")
                .email("author@example.com")
                .build());

        item1 = itemRepository.save(Item.builder()
                .name("Item One")
                .description("First item")
                .available(true)
                .owner(author)
                .build());

        item2 = itemRepository.save(Item.builder()
                .name("Item Two")
                .description("Second item")
                .available(true)
                .owner(author)
                .build());

        commentRepository.save(Comment.builder()
                .text("Great item!")
                .item(item1)
                .author(author)
                .created(LocalDateTime.now().minusHours(2))
                .build());

        commentRepository.save(Comment.builder()
                .text("Would rent again")
                .item(item1)
                .author(author)
                .created(LocalDateTime.now().minusHours(1))
                .build());

        commentB1 = commentRepository.save(Comment.builder()
                .text("Not as described")
                .item(item2)
                .author(author)
                .created(LocalDateTime.now().minusHours(3))
                .build());
    }

    @Test
    void findByItemId_ShouldReturnOnlyCommentsForThatItem() {
        List<Comment> commentsForItem1 = commentRepository.findByItemId(item1.getId());
        assertNotNull(commentsForItem1);
        assertEquals(2, commentsForItem1.size());
        assertTrue(commentsForItem1.stream().allMatch(c -> c.getItem().getId().equals(item1.getId())));

        List<Comment> commentsForItem2 = commentRepository.findByItemId(item2.getId());
        assertNotNull(commentsForItem2);
        assertEquals(1, commentsForItem2.size());
        assertEquals(commentB1.getText(), commentsForItem2.getFirst().getText());
    }

    @Test
    void findByItemId_WhenNoComments_ShouldReturnEmptyList() {
        List<Comment> none = commentRepository.findByItemId(999L);
        assertNotNull(none);
        assertTrue(none.isEmpty());
    }
}
