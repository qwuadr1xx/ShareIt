package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDtoOutForRequest;
import ru.practicum.shareit.user.dto.UserDtoForItemRequest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestDtoJsonTest {

    private JacksonTester<ItemRequestDtoOut> jsonOut;
    private JacksonTester<ItemRequestDtoIn> jsonIn;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        JacksonTester.initFields(this, mapper);
    }

    @Test
    void serializeItemRequestDtoOut() throws Exception {
        UserDtoForItemRequest requestor = UserDtoForItemRequest.builder()
                .id(7L)
                .name("Requester")
                .build();

        ItemDtoOutForRequest item = ItemDtoOutForRequest.builder()
                .id(5L)
                .name("ReqItem")
                .ownerId(3L)
                .build();

        ItemRequestDtoOut dto = ItemRequestDtoOut.builder()
                .id(2L)
                .description("Need item")
                .created(LocalDateTime.of(2025, 7, 10, 9, 15, 30))
                .requestor(requestor)
                .items(Collections.singletonList(item))
                .build();

        JsonContent<ItemRequestDtoOut> result = jsonOut.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Need item");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2025-07-10T09:15:30");
        assertThat(result).extractingJsonPathNumberValue("$.requestor.id").isEqualTo(7);
        assertThat(result).extractingJsonPathStringValue("$.requestor.name").isEqualTo("Requester");
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(5);
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo("ReqItem");
        assertThat(result).extractingJsonPathNumberValue("$.items[0].ownerId").isEqualTo(3);
    }

    @Test
    void deserializeItemRequestDtoIn() throws Exception {
        String input = """
                {
                  "description": "Need item"
                }
                """;

        ItemRequestDtoIn dto = jsonIn.parseObject(input);

        assertThat(dto.getDescription()).isEqualTo("Need item");
    }
}
