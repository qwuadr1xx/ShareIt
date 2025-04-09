package ru.practicum.shareit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotUniqueEmailException extends RuntimeException {
    private final String email;
    private final String entity;
}
