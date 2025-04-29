package ru.practicum.shareit.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.exception.BadRequestException;

@Component
public class StringToStateConverter implements Converter<String, State> {
    @Override
    public State convert(String source) {
        try {
            return State.valueOf(source.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(String.format("Такого типа State %s не существует", source),
                    "State");
        }
    }
}
