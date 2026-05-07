# FastFileSearch — High-Performance Native File Search for Java [v0.1.0]

**Low-latency, native-powered file search module for the FastJava ecosystem. Optimized for instant results across massive storage volumes.**

[![Status](https://img.shields.io/badge/status-v0.1.0--alpha-orange.svg)]()
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

---

**FastFileSearch** provides an ultra-fast alternative to standard Java file searching. By leveraging native Windows APIs and optimized search algorithms, it delivers results with zero latency.

## Table of Contents
- [Features](#features)
- [Installation](#installation)
- [License](#license)

## Features
- **⚡ Ultra-Fast Search**: High-speed discovery across millions of files.
- **🔎 Native Powered**: Direct Win32 API integration for maximum performance.
- **📦 Minimal Overhead**: Optimized for high-frequency search requests.
- **🚀 Ecosystem Ready**: Designed for use in FastRobot and FastAI agents.

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
        <groupId>io.github.andrestubbe</groupId>
        <artifactId>fastfilesearch</artifactId>
        <version>0.1.0</version>
    </dependency>

    <!-- FastCore (Required Native Loader) -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>fastcore</artifactId>
        <version>v0.1.0</version>
    </dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'io.github.andrestubbe:fastfilesearch:0.1.0'
    implementation 'com.github.andrestubbe:fastcore:v0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)
Download the latest JARs directly to add them to your classpath:

1.  📦 **[fastfilesearch-v0.1.0.jar](https://github.com/andrestubbe/fastfilesearch/releases)** (The Core Library)
2.  ⚙️ **[fastcore-v0.1.0.jar](https://github.com/andrestubbe/FastCore/releases)** (The Mandatory Native Loader)

> [!IMPORTANT]
> Both JARs must be in your classpath for the native JNI calls to function correctly.

---

## License
MIT License — See [LICENSE](LICENSE) for details.

---
**Part of the FastJava Ecosystem** — *Making the JVM faster.*
