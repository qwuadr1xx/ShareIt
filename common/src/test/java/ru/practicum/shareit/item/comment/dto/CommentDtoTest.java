package ru.practicum.shareit.item.comment.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentDtoTest {
    private JacksonTester<CommentDtoOut> jsonOut;

    private JacksonTester<CommentDtoIn> jsonIn;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        JacksonTester.initFields(this, mapper);
    }

    @Test
    void serializeCommentDtoOut() throws Exception {
        CommentDtoOut dto = CommentDtoOut.builder()
                .id(10L)
                .text("Comment")
                .authorName("User")
                .created(LocalDateTime.of(2025, 6, 15, 10, 30, 45))
                .build();

        JsonContent<CommentDtoOut> result = jsonOut.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(10);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Comment");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("User");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2025-06-15T10:30:45");
    }

    @Test
    void deserializeCommentDtoIn() throws Exception {
        String input = "{\"text\": \"comment\"}";

        CommentDtoIn dto = jsonIn.parseObject(input);

        assertThat(dto.getText()).isEqualTo("comment");
    }
}
