package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BadRequestException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@ContextConfiguration(classes = ShareItServer.class)
public class BookingControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookingService bookingService;

    @Autowired
    ObjectMapper mapper;

    private static final String BASE_PATH = "/bookings";

    private final BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
            .itemId(1L)
            .start(LocalDateTime.of(2020, 10, 10, 10, 10, 10))
            .end(LocalDateTime.of(2022, 10, 10, 10, 10, 10))
            .build();

    private final BookingDtoOut bookingDtoOut = BookingDtoOut.builder()
            .id(1L)
            .start(LocalDateTime.of(2020, 10, 10, 10, 10, 10))
            .end(LocalDateTime.of(2022, 10, 10, 10, 10, 10))
            .build();

    @Test
    void saveNewBooking_shouldReturnOkAndBookingDtoOut() throws Exception {
        when(bookingService.saveNewBooking(any(), anyLong())).thenReturn(bookingDtoOut);

        mockMvc.perform(post(BASE_PATH)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.start").value("2020-10-10T10:10:10"))
                .andExpect(jsonPath("$.end").value("2022-10-10T10:10:10"));
    }

    @Test
    void confirmBooking_shouldReturnOkAndBookingDtoOut() throws Exception {
        when(bookingService.confirmBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingDtoOut);

        mockMvc.perform(patch(BASE_PATH + "/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getBooking_shouldReturnOkAndBookingDtoOut() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong())).thenReturn(bookingDtoOut);

        mockMvc.perform(get(BASE_PATH + "/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getUserBookings_shouldReturnOkAndListOfBookings() throws Exception {
        when(bookingService.getUserBookings(anyLong(), any(), anyLong(), anyInt()))
                .thenReturn(List.of(bookingDtoOut));

        mockMvc.perform(get(BASE_PATH)
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getOwnerBookings_shouldReturnOkAndListOfBookings() throws Exception {
        when(bookingService.getOwnerBookings(anyLong(), any(), anyLong(), anyInt()))
                .thenReturn(List.of(bookingDtoOut));

        mockMvc.perform(get(BASE_PATH + "/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void saveNewBooking_withInvalidDates_shouldReturnBadRequest() throws Exception {
        BookingDtoIn invalidBookingDtoIn = BookingDtoIn.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2025, 10, 10, 10, 10, 10))
                .end(LocalDateTime.of(2022, 10, 10, 10, 10, 10))
                .build();

        when(bookingService.saveNewBooking(any(), anyLong()))
                .thenThrow(new BadRequestException(
                        "Некорректное начало 2025-10-10T10:10:10 или конец 2022-10-10T10:10:10 бронирования",
                        "Booking"));

        mockMvc.perform(post(BASE_PATH)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(invalidBookingDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Некорректное начало 2025-10-10T10:10:10 или конец 2022-10-10T10:10:10 бронирования"))
                .andExpect(jsonPath("$.description").value("Ошибка с сущностью Booking"));
    }

}
