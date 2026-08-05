package co.com.management.api.dto.response;

import lombok.experimental.UtilityClass;

import java.time.temporal.ChronoUnit;
import java.util.Map;

@UtilityClass
public class StructResponse {

    private static final Map<Integer, String> MESSAGE_MAP = Map.of(
            200, "Operation successful",
            400, "Invalid request",
            404, "Resource not found",
            500, "Internal server error",
            401, "Unauthorized"
    );


    public static <T> ResponseDTO<T> structureRS(T dto, int statusCode) {
        var metaInfo = structureMeta(MESSAGE_MAP.get(statusCode), statusCode);
        return new ResponseDTO<>(
                metaInfo,
                dto
        );
    }

    private static MetaDTO structureMeta(String message, int statusCode) {
        return new MetaDTO(
                formatDate(),
                message,
                statusCode
        );
    }
    private static String formatDate() {
        return java.time.OffsetDateTime.now(java.time.ZoneId.of("America/Bogota"))
                .truncatedTo(ChronoUnit.MILLIS)
                .toString();
    }
}
