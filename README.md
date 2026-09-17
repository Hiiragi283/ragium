![Ragium Logo](./src/main/resources/icon.png)

Ragium
=============

Ragium is a simple tech Minecraft mod for vanilla expansion and automation.

This mod is licensed under [MPL-2.0](./LICENSE)

| Version | Support |
|:-------:|:-------:|
| 1.20.1  |   ❌    |
| 1.21.1  |   ⏳    |
| 26.1.2  |   🌟    |
|  26.x   |   ⏳    |

🌟 Primary support | ✅ Supported | ⏳ Planned | ❌ Not supported yet | 🚫 Unsupported

## Downloads

|   Platform   | Main jar | Source jar | Javadoc jar |
|:------------:|:--------:|:----------:|:-----------:|
| [CurseForge] |    ✅    |     🚫     |     🚫      |
|  [Modrinth]  |    ✅    |     ✅     |     ✅      |
|   [GitHub]   |    ✅    |     ✅     |     🚫      |

### Required Dependencies

- Kotlin for Forge
    - [Download from CurseForge](https://www.curseforge.com/minecraft/mc-mods/kotlin-for-forge)
    - [Download from Modrinth](https://modrinth.com/mod/kotlin-for-forge)

### Optional Dependencies

- Just Enough Items
    - [Download from CurseForge](https://www.curseforge.com/minecraft/mc-mods/jei)
    - [Download from Modrinth](https://modrinth.com/mod/jei)

## Contribution

- [Issue Tracker](https://github.com/Hiiragi283/ragium/issues)
- [Pull Requests](https://github.com/Hiiragi283/ragium/pulls)

## Maven Repository

[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.hiiragi283/ragium?style=for-the-badge)](https://search.maven.org/artifact/io.github.hiiragi283/hiiragi-core)

![Modrinth Version](https://img.shields.io/modrinth/v/ragium?style=for-the-badge)

### Groovy

```groovy
repositories {
    // Maven Central
    mavenCentral()
    // Modrinth Maven
    // maven {
    //     url = "https://api.modrinth.com/maven"
    //     content {
    //         includeGroup "maven.modrinth"
    //     }
    // }
}

dependencies {
    // Maven Central
    implementation "io.github.hiiragi283:ragium:VERSION"
    // Modrinth Maven
    // implementation "maven.modrinth:ragium:VERSION"
}
```

### Kotlin

```kotlin
repositories {
    // Maven Central
    mavenCentral()
    // Modrinth Maven
    // maven(url = "https://api.modrinth.com/maven") {
    //     content { 
    //         includeGroup("maven.modrinth")
    //     }
    // }
}

dependencies {
    // Maven Central
    implementation("io.github.hiiragi283:ragium:VERSION")
    // Modrinth Maven
    // implementation("maven.modrinth:ragium:VERSION")
}
```

[CurseForge]: https://www.curseforge.com/minecraft/mc-mods/ragium
[Modrinth]: https://modrinth.com/mod/ragium
[GitHub]: https://github.com/Hiiragi283/ragium/releases
