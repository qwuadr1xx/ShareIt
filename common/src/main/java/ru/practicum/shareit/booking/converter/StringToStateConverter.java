package ru.practicum.shareit.booking.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.enums.State;

@Component
public class StringToStateConverter implements Converter<String, State> {
    @Override
    public State convert(String source) {
        try {
            return State.valueOf(source.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(String.format("Такого параметра state %s не существует",
                    source.trim().toUpperCase()));
        }
    }
}
