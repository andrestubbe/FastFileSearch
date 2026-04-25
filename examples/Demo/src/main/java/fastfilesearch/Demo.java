package fastfilesearch;

import fastfilesearch.FastFileSearch;
import fastfilesearch.FastFileSearch.SearchOptions;
import fastfilesearch.FastFileSearch.SearchResult;

/**
 * Demo - Demonstrates FastFileSearch capabilities.
 */
public class Demo {
    public static void main(String[] args) {
        System.out.println("=== FastFileSearch Demo ===");
        
        String indexPath = "C:\\files.idx";
        
        // Build search structures from index
        System.out.println("Building search structures from: " + indexPath);
        FastFileSearch.build(indexPath);
        
        // Prefix search (autocomplete)
        SearchOptions options = new SearchOptions()
            .limit(100)
            .fuzzy(true);
        
        String prefix = "C:\\Users\\andre\\Documents";
        System.out.println("\nPrefix search for: " + prefix);
        SearchResult[] results = FastFileSearch.prefixSearch(prefix, options);
        
        for (SearchResult result : results) {
            System.out.println(result.path() + " (score: " + result.score() + ")");
        }
        
        // Cleanup
        FastFileSearch.cleanup();
        System.out.println("\n=== Demo Complete ===");
    }
}
