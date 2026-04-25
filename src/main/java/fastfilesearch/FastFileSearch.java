package fastfilesearch;

/**
 * FastFileSearch - Builds Prefix Trie, N-Gram index, Exact Match map, and Ranking engine on top of FastFileIndex.
 * 
 * <p>FastFileSearch is the second module in the FastJava file search engine trilogy:
 * <ul>
 *   <li>FastFileIndex - Full filesystem scan → produces a binary, mmap-capable index of all files</li>
 *   <li>FastFileSearch - Builds Prefix Trie, N-Gram index, Exact Match map, and Ranking engine on top of the index</li>
 *   <li>FastFileWatch - Uses USN Journal to keep the index + search structures live-updated with zero rescans</li>
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
 *   <li>Incremental updates - Integration with FastFileWatch for live updates</li>
 * </ul>
 * 
 * @since 1.0.0
 * @version 1.0.0
 */
public class FastFileSearch {
    
    /**
     * Search result with ranking score.
     */
    public static final class SearchResult {
        private final String path;
        private final double score;
        private final long fileSize;
        private final long modifiedTime;
        
        public SearchResult(String path, double score, long fileSize, long modifiedTime) {
            this.path = path;
            this.score = score;
            this.fileSize = fileSize;
            this.modifiedTime = modifiedTime;
        }
        
        public String path() { return path; }
        public double score() { return score; }
        public long fileSize() { return fileSize; }
        public long modifiedTime() { return modifiedTime; }
    }
    
    /**
     * Search options.
     */
    public static final class SearchOptions {
        private int limit = 100;
        private boolean caseSensitive = false;
        private boolean fuzzy = true;
        private boolean includeDirectories = true;
        private boolean includeFiles = true;
        
        public SearchOptions limit(int limit) { this.limit = limit; return this; }
        public SearchOptions caseSensitive(boolean caseSensitive) { this.caseSensitive = caseSensitive; return this; }
        public SearchOptions fuzzy(boolean fuzzy) { this.fuzzy = fuzzy; return this; }
        public SearchOptions includeDirectories(boolean includeDirectories) { this.includeDirectories = includeDirectories; return this; }
        public SearchOptions includeFiles(boolean includeFiles) { this.includeFiles = includeFiles; return this; }
        
        public int limit() { return limit; }
        public boolean caseSensitive() { return caseSensitive; }
        public boolean fuzzy() { return fuzzy; }
        public boolean includeDirectories() { return includeDirectories; }
        public boolean includeFiles() { return includeFiles; }
    }
    
    /**
     * Builds search structures from FastFileIndex.
     * @param indexPath Path to the FastFileIndex file
     */
    public static native void build(String indexPath);
    
    /**
     * Performs a prefix search (autocomplete).
     * @param prefix Path prefix to search for
     * @param options Search options
     * @return List of search results
     */
    public static native SearchResult[] prefixSearch(String prefix, SearchOptions options);
    
    /**
     * Performs a fuzzy search using n-grams.
     * @param query Query string
     * @param options Search options
     * @return List of search results
     */
    public static native SearchResult[] fuzzySearch(String query, SearchOptions options);
    
    /**
     * Performs an exact match search.
     * @param filename Exact filename to search for
     * @param options Search options
     * @return List of search results
     */
    public static native SearchResult[] exactSearch(String filename, SearchOptions options);
    
    /**
     * Updates search structures incrementally (for integration with FastFileWatch).
     * @param path Path that was added/modified/deleted
     * @param type Change type (0=add, 1=modify, 2=delete)
     */
    public static native void update(String path, int type);
    
    /**
     * Releases native resources.
     */
    public static native void cleanup();
    
    static {
        try {
            System.loadLibrary("fastfilesearch");
        } catch (UnsatisfiedLinkError e1) {
            try {
                String userDir = System.getProperty("user.dir");
                String dllPath = userDir + "\\build\\fastfilesearch.dll";
                System.load(dllPath);
            } catch (UnsatisfiedLinkError e2) {
                System.err.println("Failed to load fastfilesearch.dll: " + e2.getMessage());
                throw e2;
            }
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== FastFileSearch ===");
        System.out.println("FastFileSearch - Prefix Trie, N-Gram Index, and Ranking Engine");
        System.out.println("=== OK ===");
    }
}
