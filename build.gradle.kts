import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    kotlin("jvm") version libs.versions.kotlin
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.ktlint)
}

group = "hiiragi283.ragium"
version = "0.2.0+121x"

sourceSets {
    main {
        resources {
            srcDir("src/main/generated")
        }
    }
}

repositories {
    mavenCentral()
    maven(url = "https://cursemaven.com") {
        content { includeGroup("curse.maven") }
    }
    maven(url = "https://api.modrinth.com/maven") {
        content { includeGroup("maven.modrinth") }
    }
    // AE2
    maven(url = "https://modmaven.dev/") {
        content { includeGroup("appeng") }
    }
    maven(url = "https://mod-buildcraft.com/maven") {
        content { includeGroup("alexiil.mc.lib") }
    }
    maven(url = "https://raw.githubusercontent.com/Technici4n/Technici4n-maven/master/") {
        content {
            includeGroup("dev.technici4n")
            includeGroup("net.fabricmc.fabric-api")
        }
    }
    maven(url = "https://maven.architectury.dev")
    maven(url = "https://maven.shedaniel.me") // REI
    maven(url = "https://maven.terraformersmc.com/releases")
    maven(url = "https://server.bbkr.space/artifactory/libs-release") // LibGui
    maven(url = "https://maven.ladysnake.org/releases") // CCA
    maven(url = "https://maven.wispforest.io") // oωo
}

loom {
    // accessWidenerPath = file("src/main/resources/ht_materials.accesswidener")
    splitEnvironmentSourceSets()
    mods {
        create("ragium") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.getByName("client"))
        }
    }
    runs {
        getByName("client") {
            programArg("--username=Developer")
            vmArg("-Dmixin.debug.export=true")
        }
        getByName("server") {
            runDir = "run/server"
        }
        create("datagen") {
            client()
            name = "Data Generation"
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${file("src/main/generated")}")
            vmArg("-Dfabric-api.datagen.modid=ragium")
            runDir("build/datagen")
            source(sourceSets.getByName("client"))
        }
        /*create("test") {
            inherit(getByName("client"))
            name = "Game Test Client"
            vmArg("-Dfabric-api.gametest")
            mods {
                create("ht_materials") {
                    sourceSet(sourceSets.getByName("test"))
                }
            }
        }*/
    }
}

dependencies {
    minecraft(libs.minecraft)
    mappings("net.fabricmc:yarn:${libs.versions.fabric.yarn.get()}:v2")

    modImplementation(libs.bundles.mods.fabric) {
        exclude(module = "fabric-api")
        exclude(module = "fabric-loader")
    }

    modImplementation(libs.bundles.mods.include) {
        exclude(module = "fabric-api")
        exclude(module = "fabric-loader")
    }
    include(libs.bundles.mods.include) { isTransitive = false }

    modCompileOnly(libs.bundles.mods.compile) {
        exclude(module = "fabric-api")
        exclude(module = "fabric-loader")
    }

    modLocalRuntime(libs.bundles.mods.runtime) {
        exclude(module = "fabric-api")
        exclude(module = "fabric-loader")
    }

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

ktlint {
    version = "1.3.1"
    reporters {
        reporter(ReporterType.HTML)
        reporter(ReporterType.SARIF)
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

tasks {
    test {
        useJUnitPlatform()
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
        exclude(".cache")
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${project.base.archivesName.get()}" }
        }
        exclude("**/ragium/data/**")
        exclude("**/unused/**")
    }
}
