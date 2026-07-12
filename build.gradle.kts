import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    java
}

group = "com.autoplanting"
version = "2.0.0"

val customBuildDir = findProperty("buildDir")?.toString()
if (customBuildDir != null) {
    buildDir = file(customBuildDir)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

sourceSets {
    create("legacy") {
        java.setSrcDirs(emptyList<String>())
        resources.setSrcDirs(listOf("src/legacy/resources"))
    }
    create("modern") {
        java.setSrcDirs(emptyList<String>())
        resources.setSrcDirs(listOf("src/modern/resources"))
    }
    create("current") {
        java.setSrcDirs(emptyList<String>())
        resources.setSrcDirs(listOf("src/current/resources"))
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

fun registerJarTask(taskName: String, sourceSetName: String, fileName: String) =
    tasks.register<Jar>(taskName) {
        dependsOn(tasks.named("classes"))
        archiveFileName.set(fileName)
        from(sourceSets["main"].output)
        from(sourceSets[sourceSetName].resources)
        destinationDirectory.set(layout.buildDirectory.dir("release"))
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

val legacyJar = registerJarTask("legacyJar", "legacy", "AutoPlanting-2.0.0-legacy.jar")
val modernJar = registerJarTask("modernJar", "modern", "AutoPlanting-2.0.0-modern.jar")
val currentJar = registerJarTask("currentJar", "current", "AutoPlanting-2.0.0-current.jar")

tasks.register("releaseBuild") {
    dependsOn(legacyJar, modernJar, currentJar)

    doLast {
        val releaseDir = layout.projectDirectory.dir("release").asFile
        releaseDir.mkdirs()
        releaseDir.listFiles()?.forEach { file ->
            if (file.isFile) {
                file.delete()
            }
        }

        listOf(
            legacyJar.get().archiveFile.get().asFile to "AutoPlanting-2.0.0-legacy.jar",
            modernJar.get().archiveFile.get().asFile to "AutoPlanting-2.0.0-modern.jar",
            currentJar.get().archiveFile.get().asFile to "AutoPlanting-2.0.0-current.jar",
        ).forEach { (source, name) ->
            source.copyTo(releaseDir.resolve(name), overwrite = true)
        }
    }
}
