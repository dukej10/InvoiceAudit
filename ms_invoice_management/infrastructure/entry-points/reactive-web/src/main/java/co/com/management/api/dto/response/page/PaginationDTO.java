package co.com.management.api.dto.response.page;

public record PaginationDTO(
        int page,
        int size,
        long totalItems,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {
}
