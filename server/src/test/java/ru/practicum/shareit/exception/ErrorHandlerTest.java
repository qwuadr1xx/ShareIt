package ru.practicum.shareit.exception;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Import(ErrorHandler.class)
class ErrorHandlerTest {

    private final ErrorHandler handler = new ErrorHandler();

    @Test
    void handleNotFound_ShouldReturnCorrectErrorResponse() {
        NotFoundException ex = new NotFoundException(42L, "User");
        ErrorResponse response = handler.handleNotFound(ex);

        assertNotNull(response);
        assertEquals("Ошибка с введенным id", response.getError());
        assertEquals("id 42 сущности User не находится в базе", response.getDescription());
    }

    @Test
    void handleNotOwner_ShouldReturnCorrectErrorResponse() {
        NotOwnerException ex = new NotOwnerException(15L, "Item");
        ErrorResponse response = handler.handleNotOwner(ex);

        assertNotNull(response);
        assertEquals("Ошибка с введенным id", response.getError());
        assertEquals("id 15 сущности Item не является владельцем предмета", response.getDescription());
    }

    @Test
    void handleBadRequest_ShouldReturnCorrectErrorResponse() {
        BadRequestException ex = new BadRequestException("Ошибка данных", "Booking");
        ErrorResponse response = handler.handleBadRequest(ex);

        assertNotNull(response);
        assertEquals("Ошибка данных", response.getError());
        assertEquals("Ошибка с сущностью Booking", response.getDescription());
    }

    @Test
    void handleNotUniqueEmailException_ShouldReturnCorrectErrorResponse() {
        NotUniqueEmailException ex = new NotUniqueEmailException("example@mail.com", "email");
        ErrorResponse response = handler.handleNotUniqueException(ex);

        assertNotNull(response);
        assertEquals("Данный email example@mail.com уже находится в базе", response.getError());
        assertEquals("email", response.getDescription());
    }

    @Test
    void handleEntityNotFoundException_ShouldReturnCorrectErrorResponse() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");
        ErrorResponse response = handler.handleEntityNotFoundException(ex);

        assertNotNull(response);
        assertEquals("id не найден", response.getError());
        assertEquals("Entity not found", response.getDescription());
    }
}
