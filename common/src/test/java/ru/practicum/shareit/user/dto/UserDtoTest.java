package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoTest {

    private JacksonTester<UserDto> jsonUser;
    private JacksonTester<UserDtoForItemRequest> jsonShort;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        JacksonTester.initFields(this, mapper);
    }

    @Test
    void serializeUserDto() throws Exception {
        UserDto user = UserDto.builder()
                .id(99L)
                .name("User")
                .email("User@gmail.com")
                .build();

        JsonContent<UserDto> result = jsonUser.write(user);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(99);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("User");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("User@gmail.com");
    }

    @Test
    void deserializeUserDto() throws Exception {
        String input = """
                {
                  "id": 55,
                  "name": "User",
                  "email": "User@gmail.com"
                }
                """;

        UserDto dto = jsonUser.parseObject(input);

        assertThat(dto.getId()).isEqualTo(55L);
        assertThat(dto.getName()).isEqualTo("User");
        assertThat(dto.getEmail()).isEqualTo("User@gmail.com");
    }

    @Test
    void serializeUserDtoForItemRequest() throws Exception {
        UserDtoForItemRequest dto = UserDtoForItemRequest.builder()
                .id(77L)
                .name("User")
                .build();

        JsonContent<UserDtoForItemRequest> result = jsonShort.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(77);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("User");
    }
}
