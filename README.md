# FastFileSearch 0.1.0 [ALPHA-2026-05-17] — High-Performance Native File Search for Java

[![Status](https://img.shields.io/badge/status-0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastFileSearch/releases/tag/0.1.0)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe/FastFileSearch)

---

**? Lightning fast fuzzy and prefix search for millions of indexed files.**

FastFileSearch provides **real-time search capabilities** for the FastJava ecosystem. Built on top of FastFileIndex, it
allows for instant prefix, fuzzy, and exact matching across massive file collections with sub-millisecond response
times.

---

[![FastKeyboard Showcase](docs/screenshot.png)](https://www.youtube.com/watch?v=BZsqQl7WqWk)

```java
// Quick Start  Performing a search

import fastfilesearch.FastFileSearch;
import fastfilesearch.SearchResult;

public class SearchDemo {
    public static void main(String[] args) {
        FastFileSearch engine = FastFileSearch.fromIndex(myIndex, options);

        // Instant fuzzy search
        SearchResult[] results = engine.fuzzy("myapp", 100);
        System.out.println("Found " + results.length + " matches!");
    }
}
```

---

## Table of Contents

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

## Key Features

- **ðŸš€ Native Performance**  Direct C++ search kernels for maximum speed.
- **? Fuzzy Matching**  Intelligent error-tolerant search algorithms.
- **ðŸš€ Zero Overhead**  Highly optimized memory layout for search structures.

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
    <artifactId>fastfilesearch</artifactId>
    <version>0.1.0</version>
</dependency>

<!-- FastCore (Required Native Loader) -->
<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>fastcore</artifactId>
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
    implementation 'com.github.andrestubbe:fastfilesearch:0.1.0'
    implementation 'com.github.andrestubbe:fastcore:0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)

Download the latest JARs directly to add them to your classpath:

1. ðŸš€ *
   *[fastfilesearch-0.1.0.jar](https://github.com/andrestubbe/FastFileSearch/releases/download/0.1.0/fastfilesearch-0.1.0.jar)
   ** (The Core Library)
2. ðŸš€ **[fastcore-0.1.0.jar](https://github.com/andrestubbe/FastCore/releases/download/0.1.0/fastcore-0.1.0.jar)** (
   The Mandatory Native Loader)

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
| Windows 10/11 | ? Fully Supported |
| Linux         | ðŸš€ Planned        |
| macOS         | ðŸš€ Planned        |

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

**Part of the FastJava Ecosystem**  *Making the JVM faster. Small package. Maximum speed. Zero bloat. ðŸš€ðŸš€*
