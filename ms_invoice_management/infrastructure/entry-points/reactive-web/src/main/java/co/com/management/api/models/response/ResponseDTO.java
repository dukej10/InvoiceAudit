package co.com.management.api.models.response;

import java.time.LocalDateTime;

public record ResponseDTO<T>(
        T data,
        String message,
        int statusCode,
        LocalDateTime timestamp
) {


    public ResponseDTO(T data, String message, int statusCode) {
        this(data, message, statusCode, LocalDateTime.now());
    }
}
