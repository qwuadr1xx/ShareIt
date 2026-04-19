package ru.practicum.shareit.item.comment.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {

    @Test
    void commentBuilderShouldCreateCorrectComment() {
        LocalDateTime created = LocalDateTime.now();

        User author = User.builder()
                .id(1L)
                .name("Author Name")
                .email("author@example.com")
                .build();

        Item item = Item.builder()
                .id(2L)
                .name("Item Name")
                .description("Item Description")
                .available(true)
                .build();

        Comment comment = Comment.builder()
                .id(5L)
                .text("Nice item!")
                .author(author)
                .item(item)
                .created(created)
                .build();

        assertThat(comment.getId()).isEqualTo(5L);
        assertThat(comment.getText()).isEqualTo("Nice item!");
        assertThat(comment.getAuthor()).isEqualTo(author);
        assertThat(comment.getItem()).isEqualTo(item);
        assertThat(comment.getCreated()).isEqualTo(created);
    }
}
