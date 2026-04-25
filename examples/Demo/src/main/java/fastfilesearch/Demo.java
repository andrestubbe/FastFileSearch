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
        System.out.println("FastFileSearch - Prefix Trie, N-Gram Index, and Ranking Engine");
        System.out.println();
        
        // For demo purposes, we'll use a mock implementation
        // The native layer will be implemented for production use
        System.out.println("Note: This is a demo showing the API design.");
        System.out.println("The native implementation will provide actual search functionality.");
        System.out.println();
        
        // Show API usage
        SearchOptions options = new SearchOptions()
            .limit(100)
            .fuzzy(true)
            .caseSensitive(false);
        
        System.out.println("Search Options:");
        System.out.println("  Limit: " + options.limit());
        System.out.println("  Fuzzy: " + options.fuzzy());
        System.out.println("  Case Sensitive: " + options.caseSensitive());
        System.out.println();
        
        System.out.println("=== Demo Complete ===");
    }
}
