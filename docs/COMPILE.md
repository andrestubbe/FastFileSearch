# Building FastFileSearch from Source

## Prerequisites

- **Java 17+**
- **Maven 3.6+**
- **CMake 3.15+** (for native builds, optional for Java-only)
- **Visual Studio 2019+** or **Build Tools for Visual Studio** (for Windows native builds)

## Build Steps

### 1. Clone the Repository

```bash
git clone https://github.com/andrestubbe/FastFileSearch.git
cd FastFileSearch
```

### 2. Build Java Library (Maven)

```bash
mvn clean package
```

This will generate `fastfilesearch-v1.0.0.jar` in the `target` directory.

### 3. Build Native Library (Optional)

For optimal performance, build the native library:

```bash
mkdir build
cd build
cmake .. -G "Visual Studio 16 2019" -A x64
cmake --build . --config Release
```

This will generate `fastfilesearch.dll` in the `build` directory.

## Running the Demo

```bash
cd examples/Demo
mvn exec:java
```

Or run directly:

```bash
java -cp target/fastfilesearch-v1.0.0.jar fastfilesearch.Demo
```

## Troubleshooting

### Native Library Not Found

If you see `UnsatisfiedLinkError`, ensure the native library is in the `build` directory or in your library path.

### FastFileIndex Not Found

Ensure FastFileIndex is built and the index file exists at the specified path.

## Performance Tips

- Use mmap-based index loading for large filesystems
- Enable native library for best performance
- Use prefix search for autocomplete
- Use fuzzy search for approximate matches
