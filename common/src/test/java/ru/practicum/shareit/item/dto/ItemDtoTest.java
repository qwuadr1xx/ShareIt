package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class ItemDtoTest {

    private JacksonTester<ItemDtoOut> jsonOut;
    private JacksonTester<ItemDtoIn> jsonIn;
    private JacksonTester<ItemDtoOutForRequest> jsonOutForRequest;
    private JacksonTester<ItemDtoOutShort> jsonOutShort;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        JacksonTester.initFields(this, mapper);
    }

    @Test
    void serializeItemDtoOut() throws Exception {
        ItemDtoOut dto = ItemDtoOut.builder()
                .id(100L)
                .name("ItemName")
                .description("Desc")
                .available(true)
                .comments(Collections.emptyList())
                .lastBooking(null)
                .nextBooking(null)
                .build();

        JsonContent<ItemDtoOut> result = jsonOut.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(100);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("ItemName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Desc");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
    }

    @Test
    void deserializeItemDtoIn() throws Exception {
        String input = "{\"name\": \"NewItem\", \"description\": \"NewDesc\", \"available\": false, \"requestId\": 42}";

        ItemDtoIn dto = jsonIn.parseObject(input);

        assertThat(dto.getName()).isEqualTo("NewItem");
        assertThat(dto.getDescription()).isEqualTo("NewDesc");
        assertThat(dto.getAvailable()).isFalse();
        assertThat(dto.getRequestId()).isEqualTo(42L);
    }

    @Test
    void serializeItemDtoOutForRequest() throws Exception {
        ItemDtoOutForRequest dto = ItemDtoOutForRequest.builder()
                .id(10L)
                .name("ReqItem")
                .ownerId(7L)
                .build();

        JsonContent<ItemDtoOutForRequest> result = jsonOutForRequest.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(10);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("ReqItem");
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(7);
    }

    @Test
    void serializeItemDtoOutShort() throws Exception {
        ItemDtoOutShort dto = ItemDtoOutShort.builder()
                .id(5L)
                .name("ShortName")
                .description("ShortDesc")
                .available(false)
                .build();

        JsonContent<ItemDtoOutShort> result = jsonOutShort.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(5);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("ShortName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("ShortDesc");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isFalse();
    }
}
