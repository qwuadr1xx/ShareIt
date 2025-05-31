package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.dto.ItemDtoOutShort;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingDtoTest {
    private JacksonTester<BookingDtoOut> jsonOut;

    private JacksonTester<BookingDtoIn> jsonIn;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        JacksonTester.initFields(this, mapper);
    }

    @Test
    void serializeBookingDtoOut() throws Exception {
        ItemDtoOutShort item = ItemDtoOutShort.builder()
                .id(1L)
                .name("ItemName")
                .description("ItemDescription")
                .available(true)
                .build();

        UserDto user = UserDto.builder()
                .id(1L)
                .name("UserName")
                .email("mail@gmail.com")
                .build();

        BookingDtoOut dto = BookingDtoOut.builder()
                .id(1L)
                .start(LocalDateTime.of(2025, 5, 1, 12, 0))
                .end(LocalDateTime.of(2025, 5, 2, 12, 0))
                .item(item)
                .booker(user)
                .status(Status.APPROVED)
                .build();

        JsonContent<BookingDtoOut> result = jsonOut.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2025-05-01T12:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2025-05-02T12:00:00");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("ItemName");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("UserName");
    }

    @Test
    void deserializeBookingDtoIn() throws Exception {
        String input = "{\"itemId\": 2, \"start\": \"2025-05-01T12:00:00\", \"end\": \"2025-05-02T12:00:00\"}";

        BookingDtoIn dto = jsonIn.parseObject(input);

        assertThat(dto.getItemId()).isEqualTo(2L);
        assertThat(dto.getStart()).isEqualTo(LocalDateTime.of(2025, 5, 1, 12, 0));
        assertThat(dto.getEnd()).isEqualTo(LocalDateTime.of(2025, 5, 2, 12, 0));
    }
}
