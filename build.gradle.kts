import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.slf4j.event.Level

plugins {
    idea
    id("signing")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.neo.moddev)

    alias(libs.plugins.dokka.asProvider())
    alias(libs.plugins.dokka.javadoc)
    alias(libs.plugins.spotless)

    alias(libs.plugins.axion.release)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.mod.publish)
}

val modId = "ragium"

val generateModMetadata: TaskProvider<ProcessResources> = tasks.register<ProcessResources>("generateModMetadata") {
    description = "Generate mod metadata"
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
        "mod_version" to version.toString(),
        "mod_authors" to "Hiiragi283",
        "mod_description" to "A simple tech mod for vanilla expansion and automation",
    )
    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}

scmVersion {
    useHighestVersion = true
    tag {
        prefix = "v"
        versionSeparator = ""
    }
    versionCreator("simple")
    repository {
        pushTagsOnly = true
    }
    checks {
        uncommittedChanges = false
        aheadOfRemote = false
    }
}

base {
    archivesName = modId
    group = "io.github.hiiragi283"
    version = scmVersion.version
}

val apiModule: SourceSet = sourceSets.register("api").get()
val mainModule: SourceSet = sourceSets.named("main") {
    compileClasspath += apiModule.output
    runtimeClasspath += apiModule.output

    resources {
        srcDirs("src/generated/resources", generateModMetadata.get().outputs.files)
        exclude("**/.cache/**")
    }
}.get()
val clientModule: SourceSet = sourceSets.register("client") {
    compileClasspath += mainModule.output + mainModule.compileClasspath
    runtimeClasspath += mainModule.output + mainModule.runtimeClasspath
}.get()
val integrationModule: SourceSet = sourceSets.register("integration") {
    compileClasspath += clientModule.output + clientModule.compileClasspath
    runtimeClasspath += clientModule.output + clientModule.runtimeClasspath
}.get()
val dataModule: SourceSet = sourceSets.register("data") {
    compileClasspath += integrationModule.output + integrationModule.compileClasspath
    runtimeClasspath += integrationModule.output + integrationModule.runtimeClasspath
}.get()

repositories {
    mavenLocal()

    maven(url = "https://api.modrinth.com/maven") {
        content { includeGroup("maven.modrinth") }
    }
    maven(url = "https://cursemaven.com")
    maven(url = "https://maven.parchmentmc.org")

    maven(url = "https://maven4.bai.lol/") // WTHIT
    maven(url = "https://maven.architectury.dev/") // Arch
    maven(url = "https://maven.blamejared.com/") // Patchouli, Ars, JEI
    maven(url = "https://maven.createmod.net") // Create, Flywheel
    maven(url = "https://maven.firstdark.dev/snapshots") // LDLib
    maven(url = "https://maven.ftb.dev/") // FTB
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
    maven(url = "https://modmaven.dev/") // AU, Mekanism, MI, PnC, Oritech
    maven(url = "https://mvn.devos.one/snapshots") // Registrate
    maven(url = "https://registry.somethingcatchy.net/repository/maven-releases/") // Moonlight
    maven(url = "https://thedarkcolour.github.io/KotlinForForge/") // KFF

    maven(url = "https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/") {
        content { includeGroup("software.bernie.geckolib") } // GeckoLib
    }
    maven(url = "https://dl.cloudsmith.io/public/klikli-dev/mods/maven/") {
        content { includeGroup("com.klikli_dev") } // Theurgy
    }

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

    accessTransformers {
        rootProject.fileTree("src")
            .matching { include("*/resources/META-INF/accesstransformer.cfg") }
            .forEach { atFile ->
                println("adding access transformer file: $atFile")

                from(atFile)
                publish(atFile)
            }
    }

    interfaceInjectionData {
        rootProject.fileTree("src")
            .matching { include("*/resources/META-INF/interfaceinjection.json") }
            .forEach { atFile ->
                println("adding interface injection file: $atFile")

                from(atFile)
                publish(atFile)
            }
    }

    runs {
        register("client") {
            client()
            sourceSet = clientModule

            jvmArgument("-Dmixin.debug.export=true")
            devLogin = true
        }

        register("server") {
            server()
            programArgument("--nogui")
        }

        register("integration") {
            client()
            gameDirectory = rootProject.file("run")
            sourceSet = integrationModule

            jvmArgument("-Dmixin.debug.export=true")
            devLogin = true
        }

        register("data") {
            data()
            sourceSet = dataModule

            // example of overriding the workingDirectory set in configureEach above, uncomment if you want to use it
            gameDirectory = rootProject.file("run-data")

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
            sourceSet(clientModule)
            sourceSet(integrationModule)
            sourceSet(dataModule)
        }
    }

    ideSyncTask(generateModMetadata)
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

    configurations.apply {
        runtimeClasspath.get().extendsFrom(create("localRuntime"))

        val apiCompileClasspath: Configuration = named("apiCompileClasspath").get()
        val compileClasspath: Configuration = named("compileClasspath").get()
        val clientCompileClasspath: Configuration = named("clientCompileClasspath").get()
        val integrationCompileClasspath: Configuration = named("integrationCompileClasspath").get()
        val dataCompileClasspath: Configuration = named("dataCompileClasspath").get()

        apiCompileClasspath.extendsFrom(compileClasspath)
        compileClasspath.extendsFrom(clientCompileClasspath)
        clientCompileClasspath.extendsFrom(integrationCompileClasspath)
        integrationCompileClasspath.extendsFrom(dataCompileClasspath)
    }

    implementation(libs.kff)
    implementation(libs.mek.get().toString() + ":all")
    runtimeOnly(libs.enchdesc) { exclude(group = "mezz.jei") }

    implementation(libs.bundles.common.impl)
    compileOnly(libs.bundles.common.compile)
    runtimeOnly(libs.bundles.common.runtime)

    "integrationImplementation"(libs.bundles.integration.impl)
    "integrationCompileOnly"(libs.bundles.integration.compile)
    "integrationRuntimeOnly"(libs.bundles.integration.runtime)
}

// Example configuration to allow publishing using the maven-publish plugin
pluginManager.withPlugin("com.vanniktech.maven.publish") {
    extensions.configure<MavenPublishBaseExtension> {
        publishToMavenCentral()
        signAllPublications()
        pom {
            name = "Ragium"
            description = "A simple tech mod for vanilla expansion and automation"
            inceptionYear = "2026"
            url = "https://github.com/Hiiragi283/ragium"
            scm {
                connection = "scm:git:git://github.com/Hiiragi283/ragium.git"
                developerConnection = "scm:git:git://github.com/Hiiragi283/ragium.git"
                url = "https://github.com/Hiiragi283/ragium"
            }
            licenses {
                license {
                    name = "MPL-2.0"
                    url = "https://www.mozilla.org/en-US/MPL/2.0/"
                }
            }
            developers {
                developer {
                    id = "hiiragi283"
                    name = "Hiiragi Tsubasa"
                    email = "silvengater@gmail.com"
                    url = "https://github.com/Hiiragi283/"
                }
            }
        }
    }
}

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

val jdkVersion = 21

java {
    withSourcesJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(jdkVersion)
    }
    JavaVersion.toVersion(jdkVersion).let {
        sourceCompatibility = it
        targetCompatibility = it
    }
}

kotlin {
    jvmToolchain(jdkVersion)

    compilerOptions {
        jvmTarget = JvmTarget.fromTarget("$jdkVersion")
        freeCompilerArgs.addAll()
    }
}

dokka {
    dokkaSourceSets {
        configureEach {
            sourceRoots.from(apiModule.kotlin.srcDirs, clientModule.kotlin.srcDirs, integrationModule.kotlin.srcDirs)
        }
    }
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint().editorConfigOverride(
            mapOf(
                "ktlint_standard_import-ordering" to "disabled",
                "ktlint_standard_comment-spacing" to "disabled",
            ),
        )
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
    java {
        target("src/**/*.java")
        palantirJavaFormat("2.90.0")
        endWithNewline()
        formatAnnotations()
        removeUnusedImports()
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    processResources {
        dependsOn(generateModMetadata)
    }

    jar {
        from("LICENSE") {
            rename { "${it}_ragium" }
        }
        from(apiModule.output, clientModule.output, integrationModule.output)
        from(dataModule.output) {
            this.include("**/core/data/bootstrap/**")
        }
    }

    named<Jar>("sourcesJar") {
        dependsOn("apiClasses", "clientClasses", "integrationClasses")
        duplicatesStrategy = DuplicatesStrategy.FAIL
        from(apiModule.allSource, clientModule.allSource, integrationModule.allSource)
    }

    /*wrapper {
        distributionType = Wrapper.DistributionType.BIN
    }*/
}
