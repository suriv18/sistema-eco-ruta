package pe.edu.unmsm.ciudadsana.shared.result;

import java.util.List;
import java.util.function.Function;

public record PageResult<T>(
        List<T> content,
        int page,
        int size,
        long totalElements
) {

    public PageResult {
        content = List.copyOf(content);
    }

    public static <T> PageResult<T> of(List<T> content, int page, int size, long totalElements) {
        return new PageResult<>(content, page, size, totalElements);
    }

    public static <T> PageResult<T> empty(int page, int size) {
        return new PageResult<>(List.of(), page, size, 0L);
    }

    public int totalPages() {
        return size == 0 ? 0 : (int) Math.ceil((double) totalElements / size);
    }

    public boolean hasNext() {
        return page < totalPages() - 1;
    }

    public boolean hasPrevious() {
        return page > 0;
    }

    public <U> PageResult<U> map(Function<T, U> mapper) {
        return new PageResult<>(
                content.stream().map(mapper).toList(),
                page,
                size,
                totalElements
        );
    }
}
