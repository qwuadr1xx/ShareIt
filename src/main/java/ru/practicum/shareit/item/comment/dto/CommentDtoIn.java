package ru.practicum.shareit.item.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CommentDtoIn {
    @NotBlank(message = "Text must be not blank")
    private String text;
}
