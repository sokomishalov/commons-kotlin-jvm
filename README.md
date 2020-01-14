# Kotlin/JVM Commons

[![Apache License 2](https://img.shields.io/badge/license-ASF2-purple.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)
[![](https://jitpack.io/v/sokomishalov/commons.svg)](https://jitpack.io/#sokomishalov/commons)
[![codebeat badge](https://codebeat.co/badges/b5cb2a82-6bfc-4cf6-a63f-cd69ab00f0d2)](https://codebeat.co/projects/github-com-sokomishalov-commons-master)

## Overview
Helpers and utilities for kotlin (or jvm languages)

## Installation 
Add jitpack repository:
```xml
<repositories>
	<repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Add the dependency:
```xml
<dependency>
    <groupId>com.github.sokomishalov.commons</groupId>
    <artifactId>commons-[module]</artifactId>
    <version>Tag</version>
</dependency>
```

Available modules now are:
- core
- logging
- serialization
- reactor
- coroutines
- spring
- ~~distributed-locks~~ (moved to [lokk](https://github.com/SokoMishaLov/lokk))
- ~~cache~~ (moved to [kache](https://github.com/SokoMishaLov/kache)) 
