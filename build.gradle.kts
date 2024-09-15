import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    kotlin("jvm") version libs.versions.kotlin
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.ktlint)
}

group = "hiiragi283.ragium"
version = "0.0.1+121x"

/*sourceSets {
    create("api")
    main {
        // compileClasspath.forEach { print("$it\n") }
        compileClasspath += getByName("api").output
        runtimeClasspath += getByName("api").output
    }
}

configurations {
    // names.forEach { print("$it\n") }
    getByName("apiCompileClasspath").extendsFrom(getByName("compileClasspath"))
}*/

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
    // maven(url = "https://dvs1.progwml6.com/files/maven") // JEI
    maven(url = "https://maven.architectury.dev")
    maven(url = "https://maven.shedaniel.me") // REI
    maven(url = "https://maven.terraformersmc.com/releases")
    // maven(url = "https://thedarkcolour.github.io/KotlinForForge") // KfF
    maven(url = "https://server.bbkr.space/artifactory/libs-release") // LibGui
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
    modLocalRuntime(libs.bundles.mods.debug) {
        exclude(module = "fabric-api")
        exclude(module = "fabric-loader")
    }
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

loom {
    // accessWidenerPath = file("src/main/resources/ht_materials.accesswidener")
    runs {
        getByName("client") {
            programArg("--username=Developer")
            vmArg("-Dmixin.debug.export=true")
        }
        getByName("server") {
            runDir = "run/server"
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

fabricApi {
    configureDataGeneration()
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
        exclude("**/datagen/**")
    }
}