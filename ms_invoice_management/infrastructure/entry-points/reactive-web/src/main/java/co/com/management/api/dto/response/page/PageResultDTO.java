package co.com.management.api.dto.response.page;

import java.util.List;

public record PageResultDTO<T>(
        List<T> items,
        PaginationDTO pagination
) {
}

