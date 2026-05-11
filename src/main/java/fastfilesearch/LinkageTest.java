package fastfilesearch;

import fastfileindex.FastFileIndex;
import fastfileindex.FileIndex;

public class LinkageTest {
    public static void main(String[] args) {
        System.out.println("=== FastJava Linkage Test ===");
        
        // Force load from build folders for testing
        try {
            String searchDll = new java.io.File("build/fastfilesearch.dll").getAbsolutePath();
            String indexDll = new java.io.File("../FastFileIndex/build/fastfileindex.dll").getAbsolutePath();
            System.load(searchDll);
            System.load(indexDll);
            System.out.println("    Native libraries loaded manually.");
        } catch (Throwable e) {
            System.err.println("    Warning: Manual load failed (may be okay if already loaded): " + e.getMessage());
        }
        
        try {
            System.out.println("[1] Testing FastFileIndex.build...");
            String[] roots = { "." };
            FastFileIndex.build(roots);
            System.out.println("    OK: Index built.");

            System.out.println("[2] Testing FastFileIndex.save...");
            FastFileIndex.save("test.idx");
            System.out.println("    OK: Index saved.");

            System.out.println("[3] Testing FileIndex.open (The tricky part!)...");
            FileIndex index = FileIndex.open("test.idx");
            System.out.println("    OK: FileIndex object created. Handle: " + index.handle());

            System.out.println("[4] Testing FastFileSearch.fromIndex...");
            FastFileSearch search = FastFileSearch.fromIndex(index, SearchBuildOptions.defaults());
            System.out.println("    OK: SearchEngine initialized.");

            System.out.println("\n[SUCCESS] All JNI bridges are working correctly!");
            
        } catch (Throwable e) {
            System.err.println("\n[FAILURE] Linkage error detected:");
            e.printStackTrace();
        }
    }
}
