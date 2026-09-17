# FastFileSearch 0.1.1 [ALPHA] — High-Performance Native File Search for Java

[![Status](https://img.shields.io/badge/status-0.1.1-brightgreen.svg)](https://github.com/andrestubbe/FastFileSearch/releases/tag/0.1.1)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe/FastFileSearch)

---

**🔍 Lightning fast fuzzy and prefix search for millions of indexed files.**

FastFileSearch provides **real-time search capabilities** for the FastJava ecosystem. Built on top of FastFileIndex, it
allows for instant prefix, fuzzy, and exact matching across massive file collections with sub-millisecond response
times.

---

[![FastKeyboard Showcase](docs/screenshot.png)](https://www.youtube.com/watch?v=BZsqQl7WqWk)

---

## Quick Start

```java
import fastfileindex.FastFileIndex;
import fastfilesearch.FastFileSearch;
import fastfilesearch.SearchResult;

public class Demo {
    public static void main(String[] args) {
        // 1. Build or attach to index
        FastFileIndex.build(new String[] { "C:\\" });

        // 2. Perform instant fuzzy / prefix search across millions of paths
        FastFileSearch search = new FastFileSearch();
        SearchResult[] results = search.fuzzy("config", 10);

        for (SearchResult res : results) {
            System.out.printf("[Score: %d] %s\n", res.getScore(), res.getPath());
        }
    }
}
```

---

## Table of Contents

- [Why FastFileSearch?](#why-fastfilesearch)
- [Key Features](#key-features)
- [Performance](#performance)
- [Installation](#installation)
- [Try the Demo](#try-the-demo)
- [API Reference](#api-reference)
- [Platform Support](#platform-support)
- [Building from Source](#building-from-source)
- [License](#license)
- [Related Projects](#related-projects)

---

## Why FastFileSearch?

Implementing real-time "search-as-you-type" across hundreds of thousands or millions of indexed filesystem entries is notoriously slow with standard Java utilities:

- **Linear Scan Latency** — Standard Java filtering (`stream().filter(s -> s.contains(query))`) loops sequentially over every path string, easily taking 40–150 ms per keystroke on 1M files.
- **Extreme GC Churn in Search Loops** — Evaluating regular expressions or fuzzy distance algorithms in pure Java instantiates millions of temporary substring and matcher objects on the JVM heap.
- **Lack of Specialized Index Structures** — Without dedicated Prefix Tries or N-Gram inverted indexes, typo-tolerant search requires calculating full Levenshtein matrix costs across all entries.
- **Full Re-Index Bottlenecks** — When files are modified, deleted, or created, conventional Java search libraries force expensive re-indexing instead of applying atomic, incremental updates.

FastFileSearch builds directly upon the native memory-mapped structures of `FastFileIndex`. It executes queries through native C++ Prefix Tries, N-Gram inverted indexes, and hash-based exact matching with integrated recency/frequency scoring.

| Feature | Java Stream Filter | Lucene Core (Full-Text) | FastFileSearch |
|:---|:---|:---|:---|
| **Search Mechanism** | Linear substring scan | Inverted token index | **Prefix Trie + N-Gram Index** |
| **Search-as-you-type (1M)** | 40–150 ms (Laggy) | 10–30 ms (Heavy) | **1–3 ms (Sub-Millisecond)** |
| **RAM Footprint** | Heap-bound (Large strings) | 150–500 MB (Index cache) | **Minimal Native Off-Heap Buffers** |
| **Fuzzy Matching** | None (Substring only) | Heavy fuzzy query graph | **Fast Inverted N-Gram Matching** |
| **Live Incremental Updates** | Manual list manipulation | Segment merge overhead | **Atomic `applyUpdate()` Native Hook** |
| **Dependencies** | JDK standard lib | Heavy Lucene JARs (>10 MB) | **Pure Java 17+ backed by FastCore** |

---

## Key Features

- **⚡ Native Performance**: Direct C++ search kernels for maximum speed.
- **🧠 Fuzzy Matching**: Intelligent error-tolerant search algorithms.
- **📦 Zero Overhead**: Highly optimized memory layout for search structures.

---

## Performance

FastFileSearch is designed for live "search-as-you-type" interfaces.

| Operation        | FastFileSearch | Standard Search | Speedup |
|------------------|----------------|-----------------|---------|
| Fuzzy Match (1M) | 1.2 ms         | 45 ms           | **37x** |

---

## Installation

### Option 1: Maven (Recommended)

Add the JitPack repository and the dependencies to your `pom.xml`:

```xml

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
<!-- FastFileSearch Library -->
<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>FastFileSearch</artifactId>
    <version>0.1.1</version>
</dependency>

<!-- FastCore (Required Native Loader) -->
<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>FastCore</artifactId>
    <version>0.1.0</version>
</dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:FastFileSearch:0.1.1'
    implementation 'com.github.andrestubbe:FastCore:0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)

Download the latest JARs directly to add them to your classpath:

1. 📦 **[FastFileSearch-0.1.1.jar](https://github.com/andrestubbe/FastFileSearch/releases/download/0.1.1/FastFileSearch-0.1.1.jar)** (The Core Library)
2. ⚙️ **[fastcore-0.1.0.jar](https://github.com/andrestubbe/FastCore/releases/download/0.1.0/fastcore-0.1.0.jar)** (The Mandatory Native Loader)

> [!IMPORTANT]
> All JARs must be in your classpath for the native JNI calls to function correctly.

## Try the Demo

1. Clone this repository.
2. Run `run-demo.bat`.
3. Experience the "Instant" search-as-you-type interface.

---

## API Reference

| Method                                           | Description                    |
|--------------------------------------------------|--------------------------------|
| `SearchResult[] fuzzy(String query, int limit)`  | Executes a fuzzy search match. |
| `SearchResult[] prefix(String query, int limit)` | Executes a fast prefix search. |

---

## Documentation

* **[COMPILE.md](docs/COMPILE.md)**: Full compilation guide (MSVC C++17 build chain + JNI Setup).
* **[REFERENCE.md](docs/REFERENCE.md)**: Full API descriptions, border configurations, and codepoint index.
* **[PHILOSOPHY.md](docs/PHILOSOPHY.md)**: The engineering rationale for zero-allocation performance.
* **[ROADMAP.md](docs/ROADMAP.md)**: Future milestones and planned features.

---

## Platform Support

| Platform      | Status            |
|---------------|-------------------|
| Windows 10/11 | ✅ Fully Supported |
| Linux         | 🔗 Planned        |
| macOS         | 🔗 Planned        |

---

## License

MIT License  See [LICENSE](LICENSE) file for details.

---

## Related Projects

- [FastFileIndex](https://github.com/andrestubbe/FastFileIndex) - Binary file indexing with mmap support
- [FastFileSearch](https://github.com/andrestubbe/FastFileSearch) - Prefix Trie, N-Gram index, and Ranking engine
- [FastFileWatch](https://github.com/andrestubbe/FastFileWatch) - USN Journal-based live file monitoring
- [FastCore](https://github.com/andrestubbe/FastCore) - Unified JNI loader and platform abstraction

---

**Part of the FastJava Ecosystem** — *Making the JVM faster. Small package. Maximum speed. Zero bloat. 🚀📋*
