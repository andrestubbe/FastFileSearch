package fastfilesearch;

import fastfileindex.FileIndex;

/**
 * SearchEngine - Handle-based search engine with Trie, N-Gram, and Ranking.
 * 
 * <p>SearchEngine is the second module in the FastJava file search engine trilogy:
 * <ul>
 *   <li>FileIndex - Full filesystem scan → produces a binary, mmap-capable index of all files</li>
 *   <li>SearchEngine - Builds Prefix Trie, N-Gram index, Exact Match map, and Ranking engine on top of the index</li>
 *   <li>WatchService - Uses USN Journal to keep the index + search structures live-updated with zero rescans</li>
 * </ul>
 * 
 * <p>This architecture is similar to Everything, Spotlight, VSCode, and fsearch but modular and embeddable.
 * 
 * <p><b>Key Features:</b>
 * <ul>
 *   <li>Prefix Trie - Fast autocomplete for path prefixes</li>
 *   <li>N-Gram Index - Fuzzy search with character n-grams</li>
 *   <li>Exact Match Map - O(1) exact filename lookups</li>
 *   <li>Ranking Engine - Recency, frequency, and path-based scoring</li>
 *   <li>Zero-alloc query execution - Minimal allocations during search</li>
 *   <li>Incremental updates - Integration with WatchService for live updates</li>
 * </ul>
 * 
 * @since 1.0.0
 * @version 1.0.0
 */
public final class SearchEngine {
    static {
        try {
            System.loadLibrary("fastcore");
        } catch (UnsatisfiedLinkError e1) {
            try {
                String userDir = System.getProperty("user.dir");
                String dllPath = userDir + "\\build\\fastcore.dll";
                System.load(dllPath);
            } catch (UnsatisfiedLinkError e2) {
                System.err.println("Failed to load fastcore.dll: " + e2.getMessage());
                throw e2;
            }
        }
    }

    private final long nativeHandle;

    private SearchEngine(long nativeHandle) {
        this.nativeHandle = nativeHandle;
    }

    public long handle() {
        return nativeHandle;
    }

    /**
     * Build search engine from index.
     */
    public static native SearchEngine fromIndex(FileIndex index, SearchBuildOptions options);

    /**
     * Close search engine and free resources.
     */
    public native void close();

    /**
     * Prefix search (autocomplete).
     */
    public native SearchResult[] prefix(SearchQuery query, SearchOptions options);

    /**
     * Fuzzy search using n-grams.
     */
    public native SearchResult[] fuzzy(SearchQuery query, SearchOptions options);

    /**
     * Exact match search.
     */
    public native SearchResult[] exact(SearchQuery query, SearchOptions options);

    /**
     * Apply file update (for live updates).
     */
    public native void applyUpdate(FileUpdate update);

    public static void main(String[] args) {
        System.out.println("=== SearchEngine ===");
        System.out.println("SearchEngine - Prefix Trie, N-Gram Index, and Ranking Engine");
        System.out.println("=== OK ===");
    }
}
