package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.enums.State;

import java.util.List;
import java.util.Map;

@Service
public class
BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    public BookingClient(@Value("${shareit-server.url}") String serverUrl) {
        super(RestClient.builder().baseUrl(serverUrl + API_PREFIX).build(), serverUrl + API_PREFIX);
    }

    public ResponseEntity<BookingDtoOut> bookItem(long userId, BookingDtoIn dto) {
        return post("", userId, dto, BookingDtoOut.class);
    }

    public ResponseEntity<BookingDtoOut> confirmBooking(Long bookingId, Long userId, boolean approve) {
        return patch("/" + bookingId, userId,
                Map.of("approved", approve), BookingDtoOut.class);
    }

    public ResponseEntity<BookingDtoOut> getBooking(long bookingId, long userId) {
        return get("/" + bookingId, userId, BookingDtoOut.class);
    }

    public ResponseEntity<List<BookingDtoOut>> getBookings(long userId, State state, long from, int size) {
        return getList("", userId,
                Map.of("state", state, "from", from, "size", size), BookingDtoOut[].class);
    }

    public ResponseEntity<List<BookingDtoOut>> getOwnerBookings(long userId, State state, long from, int size) {
        return getList("", userId,
                Map.of("state", state.name(), "from", from, "size", size), BookingDtoOut[].class);
    }
}
