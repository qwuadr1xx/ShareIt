package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UserDto {
    private final Long id;

    @NotBlank(message = "Name can't be blank")
    private final String name;

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email must comply with the standard")
    private final String email;
}
