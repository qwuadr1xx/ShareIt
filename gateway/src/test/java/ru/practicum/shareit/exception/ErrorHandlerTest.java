package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import ru.practicum.shareit.ShareItGateway;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Import(ErrorHandler.class)
@SpringBootTest(classes = ShareItGateway.class)
class ErrorHandlerTest {
    private final ErrorHandler handler = new ErrorHandler();

    @Test
    void handleHttpStatusCodeException_ShouldReturnCorrectResponse() {
        HttpStatusCodeException ex = new HttpStatusCodeException(
                HttpStatus.NOT_FOUND, "Not Found"
        ) {
            @Override
            public String getResponseBodyAsString() {
                return "Resource missing";
            }
        };

        ResponseEntity<String> resp = handler.handleHttpStatusCodeException(ex);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertEquals("Resource missing", resp.getBody());
    }
}
