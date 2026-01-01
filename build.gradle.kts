import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import org.slf4j.event.Level

plugins {
    idea
    kotlin("jvm") version "2.2.20"
    alias(libs.plugins.neo.moddev)
    alias(libs.plugins.ktlint)
}

val modId = "ragium"

version = libs.versions.ragium.get()
group = "hiiragi283.ragium"
base.archivesName = modId

val apiModule: SourceSet = sourceSets.create("api")
val dataModule: SourceSet = sourceSets.create("data")

sourceSets {
    main {
        compileClasspath += apiModule.output
        runtimeClasspath += apiModule.output
        resources {
            srcDir("src/generated/resources")
        }
    }
    getByName("data") {
        val main: SourceSet by main
        compileClasspath += main.compileClasspath
        compileClasspath += main.output
        runtimeClasspath += main.runtimeClasspath
        runtimeClasspath += main.output
    }
}

// Sets up a dependency configuration called 'localRuntime'.
// This configuration should be used instead of 'runtimeOnly' to declare
// a dependency that will be present for runtime testing but that is
// "optional", meaning it will not be pulled by dependents of this mod.
configurations.apply {
    runtimeClasspath.get().extendsFrom(create("localRuntime"))

    getByName("apiCompileClasspath").extendsFrom(getByName("compileClasspath"))
    getByName("compileClasspath").extendsFrom(getByName("dataCompileClasspath"))
}

repositories {
    mavenLocal()

    maven(url = "https://api.modrinth.com/maven") {
        content { includeGroup("maven.modrinth") }
    }
    maven(url = "https://cursemaven.com")
    maven(url = "https://maven.parchmentmc.org")
    maven(url = "https://modmaven.dev/") // AU, Mekanism, PnC, Oritech

    maven(url = "https://maven4.bai.lol/") // WTHIT
    maven(url = "https://maven.architectury.dev/") // Arch
    maven(url = "https://maven.blamejared.com/") // Patchouli, Ars
    maven(url = "https://maven.createmod.net") // Create, Flywheel
    maven(url = "https://maven.k-4u.nl/") // TOP
    maven(url = "https://maven.rover656.dev/releases") // EIO
    maven(url = "https://maven.saps.dev/releases") // AA
    maven(url = "https://maven.shadowsoffire.dev/releases") // HNN
    maven(url = "https://maven.su5ed.dev/releases") // FFAPI
    maven(url = "https://maven.tamaized.com/releases") // Twilight
    maven(url = "https://maven.teamresourceful.com/repository/maven-public/") // Athena
    maven(url = "https://maven.terraformersmc.com/") // EMI
    maven(url = "https://maven.theillusivec4.top/") // Curios
    maven(url = "https://maven.wispforest.io/releases") // Accessories
    maven(url = "https://mvn.devos.one/snapshots") // Registrate
    maven(url = "https://thedarkcolour.github.io/KotlinForForge/") // KFF

    maven(url = "https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/") {
        content { includeGroup("software.bernie.geckolib") } // GeckoLib
    }
    maven(url = "https://dl.cloudsmith.io/public/klikli-dev/mods/maven/") {
        content { includeGroup("com.klikli_dev") } // Theurgy
    }
    maven(url = "https://maven.pkg.github.com/refinedmods/refinedstorage2") {
        credentials {
            username = "anything"
            password = "\u0067hp_oGjcDFCn8jeTzIj4Ke9pLoEVtpnZMP4VQgaX"
        }
    } // RS2

    mavenCentral()
}

// Mojang ships Java 21 to end users starting in 1.20.5, so mods should target Java 21.
// java.toolchain.languageVersion = JavaLanguageVersion.of(21)

neoForge {
    // Specify the version of NeoForge to use.
    version = libs.versions.neo.version
        .get()

    parchment {
        mappingsVersion = libs.versions.parchment.map
            .get()
        minecraftVersion = libs.versions.parchment.mc
            .get()
    }

    // This line is optional. Access Transformers are automatically detected
    // accessTransformers = project.files("src/main/resources/META-INF/accesstransformer.cfg")

    // Default run configurations.
    // These can be tweaked, removed, or duplicated as needed.
    runs {
        create("client").apply {
            client()

            // Comma-separated list of namespaces to load gametests from. Empty = all namespaces.
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
            jvmArgument("-Dmixin.debug.export=true")
            devLogin = true
        }

        create("server").apply {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        // This run config launches GameTestServer and runs all registered gametests, then exits.
        // By default, the server will crash when no gametests are provided.
        // The gametest system is also enabled by default for other run configs under the /test command.
        create("gameTestServer").apply {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        create("data").apply {
            data()
            sourceSet = dataModule

            // example of overriding the workingDirectory set in configureEach above, uncomment if you want to use it
            // gameDirectory = project.file("run-data")

            // Specify the modid for data generation, where to output the resulting resource, and where to look for existing resources.
            programArguments.addAll(
                "--mod",
                modId,
                "--all",
                "--output",
                file("src/generated/resources/").absolutePath,
                "--existing",
                file("src/main/resources").absolutePath,
                "--existing-mod",
                "hiiragi_core",
            )
        }

        // applies to all the run configs above
        configureEach {
            // Recommended logging data for a userdev environment
            // The markers can be added/remove as needed separated by commas.
            // "SCAN": For mods scan.
            // "REGISTRIES": For firing of registry events.
            // "REGISTRYDUMP": For getting the contents of all registries.
            systemProperty("forge.logging.markers", "REGISTRIES")

            // Recommended logging level for the console
            // You can set various levels here.
            // Please read: https://stackoverflow.com/questions/2031163/when-to-use-the-different-log-levels
            logLevel = Level.DEBUG
        }
    }

    mods {
        // define mod <-> source bindings
        // these are used to tell the game which sources are for which mod
        // multi mod projects should define one per mod
        create(modId) {
            sourceSet(sourceSets.main.get())
            sourceSet(apiModule)
            sourceSet(dataModule)
        }
    }
}

dependencies {
    // Example optional mod dependency with JEI
    // The JEI API is declared for compile time use, while the full JEI artifact is used at runtime
    // compileOnly "mezz.jei:jei-${mc_version}-common-api:${jei_version}"
    // compileOnly "mezz.jei:jei-${mc_version}-neoforge-api:${jei_version}"
    // We add the full version to localRuntime, not runtimeOnly, so that we do not publish a dependency on it
    // localRuntime "mezz.jei:jei-${mc_version}-neoforge:${jei_version}"

    // Example mod dependency using a mod jar from ./libs with a flat dir repository
    // This maps to ./libs/coolmod-${mc_version}-${coolmod_version}.jar
    // The group id is ignored when searching -- in this case, it is "blank"
    // implementation "blank:coolmod-${mc_version}:${coolmod_version}"

    // Example mod dependency using a file as dependency
    // implementation files("libs/coolmod-${mc_version}-${coolmod_version}.jar")

    // Example project dependency using a sister or child project:
    // implementation project(":myproject")

    // For more info:
    // http://www.gradle.org/docs/current/userguide/artifact_dependencies_tutorial.html
    // http://www.gradle.org/docs/current/userguide/dependency_management.html

    implementation(libs.kff)

    implementation(libs.bundles.mods.impl)
    implementation(libs.bundles.mods.transitive) { isTransitive = false }
    compileOnly(libs.bundles.mods.compile)
    runtimeOnly(libs.bundles.mods.runtime)

    implementation(libs.mek.get().toString() + ":all")

    implementation(libs.enchdesc) {
        exclude(group = "mezz.jei")
    }
}

// This block of code expands all declared replace properties in the specified resource targets.
// A missing property will result in an error. Properties are expanded using ${} Groovy notation.
val generateModMetadata: TaskProvider<ProcessResources> = tasks.register("generateModMetadata", ProcessResources::class.java) {
    val mcVersion: String = libs.versions.minecraft.get()
    val neoVersion: String = libs.versions.neo.version
        .get()
    val kffVersion: String = libs.versions.kff.version
        .get()

    val replaceProperties: Map<String, String> = mapOf(
        "minecraft_version" to mcVersion,
        "minecraft_version_range" to "[$mcVersion]",
        "neo_version" to neoVersion,
        "neo_version_range" to "[$neoVersion,)",
        "kff_version" to kffVersion,
        "kff_version_range" to "[$kffVersion,)",
        "loader_version_range" to "[1,)",
        "mod_id" to modId,
        "mod_name" to "Ragium",
        "mod_license" to "MPL-2.0",
        "mod_version" to libs.versions.ragium.get(),
        "mod_authors" to "Hiiragi283",
        "mod_description" to
            "Ragium is a tech mod based on vanilla materials. This mod aims to expand vanilla features and automate many work.",
    )
    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}
// Include the output of "generateModMetadata" as an input directory for the build
// this works with both building through Gradle and the IDE.
sourceSets.main
    .get()
    .resources
    .srcDir(generateModMetadata)
// To avoid having to run "generateModMetadata" manually, make it run on every project reload
neoForge.ideSyncTask(generateModMetadata)

// Example configuration to allow publishing using the maven-publish plugin
/*publishing {
    publications {
        register("mavenJava", MavenPublication) {
            from(components.java)
        }
    }
    repositories {
        maven {
            url("file://${project.projectDir}/repo")
        }
    }
}*/

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    processResources {
        exclude("**/.cache/**")
    }

    jar {
        from("LICENSE") {
            rename { "${it}_ragium" }
        }
        from(apiModule.output)
        from(dataModule.output)
        exclude("**/ragium/data/**")
        exclude("**/unused/**")
    }

    /*wrapper {
        distributionType = Wrapper.DistributionType.BIN
    }*/
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

ktlint {
    version = "1.5.0"
    reporters {
        reporter(ReporterType.JSON)
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}
