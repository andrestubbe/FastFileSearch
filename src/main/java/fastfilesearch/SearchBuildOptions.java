package fastfilesearch;

/**
 * SearchBuildOptions - Options for building the search engine.
 */
public class SearchBuildOptions {
    private boolean enableTrie = true;
    private boolean enableNgram = true;
    private boolean enableExact = true;
    private int ngramSize = 3;

    public SearchBuildOptions() {}

    public SearchBuildOptions enableTrie(boolean value) {
        this.enableTrie = value;
        return this;
    }

    public SearchBuildOptions enableNgram(boolean value) {
        this.enableNgram = value;
        return this;
    }

    public SearchBuildOptions enableExact(boolean value) {
        this.enableExact = value;
        return this;
    }

    public SearchBuildOptions ngramSize(int value) {
        this.ngramSize = value;
        return this;
    }

    public boolean enableTrie() {
        return enableTrie;
    }

    public boolean enableNgram() {
        return enableNgram;
    }

    public boolean enableExact() {
        return enableExact;
    }

    public int ngramSize() {
        return ngramSize;
    }

    public static SearchBuildOptions defaults() {
        return new SearchBuildOptions();
    }
}
