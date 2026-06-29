package pe.edu.unmsm.ciudadsana.shared.web.request;

public record PaginationRequest(
        int page,
        int size,
        String sortBy,
        String sortDir
) {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;
    private static final String DEFAULT_SORT_DIR = "asc";

    public PaginationRequest {
        if (page < 0) page = DEFAULT_PAGE;
        if (size <= 0) size = DEFAULT_SIZE;
        if (size > MAX_SIZE) size = MAX_SIZE;
        if (sortDir == null) sortDir = DEFAULT_SORT_DIR;
    }

    public static PaginationRequest defaults() {
        return new PaginationRequest(DEFAULT_PAGE, DEFAULT_SIZE, null, DEFAULT_SORT_DIR);
    }

    public static PaginationRequest of(int page, int size) {
        return new PaginationRequest(page, size, null, DEFAULT_SORT_DIR);
    }

    public boolean isAscending() {
        return "asc".equalsIgnoreCase(sortDir);
    }
}
