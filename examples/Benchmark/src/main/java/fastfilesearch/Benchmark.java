package fastfilesearch;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Benchmark - Compares SearchEngine to Java's normal file search.
 */
public class Benchmark {
    
    public static void main(String[] args) {
        System.out.println("=== SearchEngine Benchmark ===");
        System.out.println("Comparing SearchEngine to Java's normal file search");
        System.out.println();
        
        String searchPath = "C:\\";
        String query = "Documents";
        
        System.out.println("Search Path: " + searchPath);
        System.out.println("Query: " + query);
        System.out.println();
        
        // Benchmark Java normal search
        long javaTime = benchmarkJavaSearch(searchPath, query);
        System.out.println("Java Search Time: " + javaTime + " ms");
        
        // Benchmark SearchEngine (Java implementation for now)
        long fastSearchTime = benchmarkSearchEngine(searchPath, query);
        System.out.println("SearchEngine Time: " + fastSearchTime + " ms");
        
        System.out.println();
        double speedup = (double) javaTime / fastSearchTime;
        System.out.println("Speedup: " + String.format("%.2f", speedup) + "x");
        
        System.out.println();
        System.out.println("Note: SearchEngine currently uses Java stub implementation.");
        System.out.println("Native implementation with Prefix Trie and N-Gram Index will provide significant performance improvements.");
        System.out.println();
        System.out.println("=== Benchmark Complete ===");
    }
    
    private static long benchmarkJavaSearch(String searchPath, String query) {
        System.out.println("Running Java normal search...");
        long startTime = System.currentTimeMillis();
        
        List<String> results = new ArrayList<>();
        try {
            Files.walkFileTree(Paths.get(searchPath), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    String fileName = file.getFileName().toString();
                    if (fileName.toLowerCase().contains(query.toLowerCase())) {
                        results.add(file.toString());
                    }
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.err.println("Error during Java search: " + e.getMessage());
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("Found " + results.size() + " files");
        return endTime - startTime;
    }
    
    private static long benchmarkSearchEngine(String searchPath, String query) {
        System.out.println("Running SearchEngine (Java stub)...");
        long startTime = System.currentTimeMillis();
        
        // For now, use Java implementation since native layer is stub
        // This will be replaced with native implementation for production
        List<String> results = new ArrayList<>();
        try {
            Files.walkFileTree(Paths.get(searchPath), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    String fileName = file.getFileName().toString();
                    if (fileName.toLowerCase().contains(query.toLowerCase())) {
                        results.add(file.toString());
                    }
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.err.println("Error during SearchEngine: " + e.getMessage());
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("Found " + results.size() + " files");
        return endTime - startTime;
    }
}
