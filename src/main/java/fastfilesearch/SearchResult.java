package fastfilesearch;

/**
 * SearchResult - Search result value type.
 */
public record SearchResult(
        String path,
        double score,
        long fileSize,
        long modifiedTime
) {}
