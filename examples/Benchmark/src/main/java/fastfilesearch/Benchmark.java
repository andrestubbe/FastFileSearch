package fastfilesearch;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Benchmark - Compares FastFileSearch to Java's normal file search.
 */
public class Benchmark {
    
    public static void main(String[] args) {
        System.out.println("=== FastFileSearch Benchmark ===");
        System.out.println("Comparing FastFileSearch to Java's normal file search");
        System.out.println();
        
        String searchPath = "C:\\";
        String query = "Documents";
        
        System.out.println("Search Path: " + searchPath);
        System.out.println("Query: " + query);
        System.out.println();
        
        // Benchmark Java normal search
        long javaTime = benchmarkJavaSearch(searchPath, query);
        System.out.println("Java Search Time: " + javaTime + " ms");
        
        // Benchmark FastFileSearch (Java implementation for now)
        long fastSearchTime = benchmarkFastFileSearch(searchPath, query);
        System.out.println("FastFileSearch Time: " + fastSearchTime + " ms");
        
        System.out.println();
        double speedup = (double) javaTime / fastSearchTime;
        System.out.println("Speedup: " + String.format("%.2f", speedup) + "x");
        
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
    
    private static long benchmarkFastFileSearch(String searchPath, String query) {
        System.out.println("Running FastFileSearch...");
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
            System.err.println("Error during FastFileSearch: " + e.getMessage());
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("Found " + results.size() + " files");
        return endTime - startTime;
    }
}
