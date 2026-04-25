package fastfilesearch;

/**
 * SearchOptions - Search options value type.
 */
public class SearchOptions {
    private int limit = 100;
    private boolean caseSensitive = false;
    private boolean includeDirectories = true;
    private boolean includeFiles = true;

    public SearchOptions() {}

    public SearchOptions limit(int value) {
        this.limit = value;
        return this;
    }

    public SearchOptions caseSensitive(boolean value) {
        this.caseSensitive = value;
        return this;
    }

    public SearchOptions includeDirectories(boolean value) {
        this.includeDirectories = value;
        return this;
    }

    public SearchOptions includeFiles(boolean value) {
        this.includeFiles = value;
        return this;
    }

    public int limit() {
        return limit;
    }

    public boolean caseSensitive() {
        return caseSensitive;
    }

    public boolean includeDirectories() {
        return includeDirectories;
    }

    public boolean includeFiles() {
        return includeFiles;
    }

    public static SearchOptions defaults() {
        return new SearchOptions();
    }
}
