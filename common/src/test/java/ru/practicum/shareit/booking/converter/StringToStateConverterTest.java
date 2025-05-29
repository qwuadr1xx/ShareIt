package ru.practicum.shareit.booking.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.State;

import static org.junit.jupiter.api.Assertions.*;


public class StringToStateConverterTest {
    private StringToStateConverter converter;

    @BeforeEach
    void setUp() {
        this.converter = new StringToStateConverter();
    }

    @Test
    void convert_shouldReturnCorrectStateIgnoringCaseAndSpaces() {
        assertEquals(State.ALL, converter.convert("ALL"));
        assertEquals(State.ALL, converter.convert("aLl "));
        assertEquals(State.CURRENT, converter.convert("CURRENT"));
        assertEquals(State.CURRENT, converter.convert(" current"));
        assertEquals(State.PAST, converter.convert("pAsT"));
        assertEquals(State.FUTURE, converter.convert("FUTURE "));
        assertEquals(State.WAITING, converter.convert(" waiting "));
        assertEquals(State.REJECTED, converter.convert("ReJecTEd"));
    }

    @Test
    void convert_shouldThrowExceptionWhenStateIsWrong() {
        RuntimeException e = assertThrows(RuntimeException.class,
                () -> converter.convert("unknown"));

        assertEquals("Такого параметра state UNKNOWN не существует", e.getMessage());
    }
}
