import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    base
}

allprojects {
    group = "com.autoplanting"
    version = "2.0.0"

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
}

subprojects {
    apply(plugin = "java-library")

    extensions.configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(8))
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}

tasks.register("releaseBuild") {
    dependsOn(
        ":distribution-legacy:jar",
        ":distribution-modern:jar",
        ":distribution-current:jar",
    )

    doLast {
        val releaseDir = layout.projectDirectory.dir("release").asFile
        releaseDir.mkdirs()

        releaseDir.listFiles()?.forEach { file ->
            if (file.isFile) {
                file.delete()
            }
        }

        val copies = listOf(
            project(":distribution-legacy").tasks.named("jar").get().outputs.files.singleFile to "AutoPlanting-2.0.0-legacy.jar",
            project(":distribution-modern").tasks.named("jar").get().outputs.files.singleFile to "AutoPlanting-2.0.0-modern.jar",
            project(":distribution-current").tasks.named("jar").get().outputs.files.singleFile to "AutoPlanting-2.0.0-current.jar",
        )

        copies.forEach { (source, name) ->
            source.copyTo(releaseDir.resolve(name), overwrite = true)
        }
    }
}
