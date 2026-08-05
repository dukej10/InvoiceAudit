package co.com.management.api.dto.response;

import java.time.LocalDateTime;

public record ResponseDTO<T>(
        MetaDTO meta,
        T data
) {
}
