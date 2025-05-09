package ru.practicum.shareit.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        return new ErrorResponse(
                "Ошибка с введенным id",
                String.format("id %s сущности %s не находится в базе", e.getId(), e.getEntity())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotOwner(final NotOwnerException e) {
        return new ErrorResponse(
                "Ошибка с введенным id",
                String.format("id %s сущности %s не является владельцем предмета", e.getId(), e.getEntity())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(final BadRequestException e) {
        return new ErrorResponse(
                e.getError(),
                String.format("Ошибка с сущностью %s", e.getEntity())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleNotUniqueException(final NotUniqueEmailException e) {
        return new ErrorResponse(
                String.format("Данный email %s уже находится в базе", e.getEmail()),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(final EntityNotFoundException e) {
        return new ErrorResponse(
                "id не найден",
                e.getMessage()
        );

    }
}
