package fastfilesearch;

/**
 * FileUpdate - File update value type.
 */
public record FileUpdate(
        FileUpdateType type,
        String oldPath,
        String newPath
) {}
