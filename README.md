# FastFileSearch

[![Build](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Cross%20Platform-lightgrey.svg)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![JitPack](https://jitpack.io/v/andrestubbe/FastFileSearch.svg)](https://jitpack.io/#andrestubbe/FastFileSearch)

## Table of Contents

- [Description](#description)
- [Quick Start](#quick-start)
- [Key Features](#key-features)
- [Installation](#installation)
- [Building from Source](#building-from-source)
- [Platform Support](#platform-support)
- [License](#license)
- [Related Projects](#related-projects)

## Description

FastFileSearch builds Prefix Trie, N-Gram index, Exact Match map, and Ranking engine on top of FastFileIndex. It is the second module in the FastJava file search engine trilogy:

- **FastFileIndex** - Full filesystem scan → produces a binary, mmap-capable index of all files
- **FastFileSearch** - Builds Prefix Trie, N-Gram index, Exact Match map, and Ranking engine on top of the index
- **FastFileWatch** - Uses USN Journal to keep the index + search structures live-updated with zero rescans

This architecture is similar to Everything, Spotlight, VSCode, and fsearch but modular and embeddable.

## Quick Start

```java
import fastfilesearch.FastFileSearch;
import fastfilesearch.FastFileSearch.SearchOptions;
import fastfilesearch.FastFileSearch.SearchResult;

public class Example {
    public static void main(String[] args) {
        // Build search structures from index
        FastFileSearch.build("C:\\files.idx");
        
        // Prefix search (autocomplete)
        SearchOptions options = new SearchOptions()
            .limit(100)
            .fuzzy(true);
        
        SearchResult[] results = FastFileSearch.prefixSearch("C:\\Users\\andre\\Documents", options);
        
        for (SearchResult result : results) {
            System.out.println(result.path() + " (score: " + result.score() + ")");
        }
        
        // Cleanup
        FastFileSearch.cleanup();
    }
}
```

## Key Features

- **Prefix Trie** - Fast autocomplete for path prefixes
- **N-Gram Index** - Fuzzy search with character n-grams
- **Exact Match Map** - O(1) exact filename lookups
- **Ranking Engine** - Recency, frequency, and path-based scoring
- **Zero-alloc query execution** - Minimal allocations during search
- **Incremental updates** - Integration with FastFileWatch for live updates

## Installation

### Maven (JitPack)

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>fastfilesearch</artifactId>
    <version>v1.0.0</version>
</dependency>
<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>fastfileindex</artifactId>
    <version>v1.0.0</version>
</dependency>
<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>fastcore</artifactId>
    <version>v1.0.0</version>
</dependency>
```

### Gradle (JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:fastfilesearch:v1.0.0'
    implementation 'com.github.andrestubbe:fastfileindex:v1.0.0'
    implementation 'com.github.andrestubbe:fastcore:v1.0.0'
}
```

## Building from Source

For detailed build instructions, see [COMPILE.md](COMPILE.md).

## Platform Support

- **Windows 10+** (x86_64) - Fully supported
- **Linux** - Planned
- **macOS** - Planned

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Related Projects

- [FastFileIndex](https://github.com/andrestubbe/FastFileIndex) - Binary file indexing with mmap support
- [FastFileWatch](https://github.com/andrestubbe/FastFileWatch) - USN Journal-based live file monitoring
- [FastCore](https://github.com/andrestubbe/FastCore) - Unified JNI loader and platform abstraction
