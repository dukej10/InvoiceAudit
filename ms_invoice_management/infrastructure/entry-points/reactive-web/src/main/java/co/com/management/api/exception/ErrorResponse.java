package co.com.management.api.exception;

import java.util.Date;
import java.util.List;

public record ErrorResponse(
        Date timestamp,
        String message,
        String details,
        List<FieldError> fieldErrors
) {
    public record FieldError(String field, String error) {}
}
