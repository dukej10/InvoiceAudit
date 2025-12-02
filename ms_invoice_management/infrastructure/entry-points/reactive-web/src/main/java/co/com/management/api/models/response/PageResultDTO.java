package co.com.management.api.models.response;

import java.util.List;

public record PageResultDTO<T> (
        List<T> items,
        int page,
        int size,
        long totalItems,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
)
{
    

}

