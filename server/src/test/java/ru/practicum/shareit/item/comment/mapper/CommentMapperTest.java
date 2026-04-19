package ru.practicum.shareit.item.comment.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentMapperTest {

    @Test
    void toCommentDto_shouldMapCommentToDtoCorrectly() {
        LocalDateTime created = LocalDateTime.now();

        User author = User.builder()
                .id(1L)
                .name("Author Name")
                .email("author@gmail.com")
                .build();

        Item item = Item.builder()
                .id(2L)
                .name("Item Name")
                .description("Item Description")
                .available(true)
                .build();

        Comment comment = Comment.builder()
                .id(10L)
                .text("Comment")
                .author(author)
                .item(item)
                .created(created)
                .build();

        CommentDtoOut dto = CommentMapper.toCommentDto(comment);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getText()).isEqualTo("Comment");
        assertThat(dto.getAuthorName()).isEqualTo("Author Name");
        assertThat(dto.getCreated()).isEqualTo(created);
    }
}
