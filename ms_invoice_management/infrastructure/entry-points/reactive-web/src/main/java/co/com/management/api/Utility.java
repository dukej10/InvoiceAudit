package co.com.management.api;

import co.com.management.api.dto.response.ResponseDTO;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class Utility {

    private static final Map<Integer, String> MESSAGE_MAP = Map.of(
            200, "Operation successful",
            400, "Invalid request",
            404, "Resource not found",
            500, "Internal server error",
            401, "Unauthorized"
    );


    public static <T> ResponseDTO<T> structureRS(T dto, int statusCode) {
        return new ResponseDTO<>(
                dto,
                MESSAGE_MAP.get(statusCode),
                statusCode
        );
    }
}