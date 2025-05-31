package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Name can't be blank")
    private String name;

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email must comply with the standard")
    private String email;
}
