dependencies {
    implementation(project(":plugin-common"))
    implementation(project(":platform-bukkit"))
    implementation(project(":version-legacy"))
}

tasks.jar {
    dependsOn(
        project(":plugin-api").tasks.named("jar"),
        project(":plugin-common").tasks.named("jar"),
        project(":platform-bukkit").tasks.named("jar"),
        project(":version-legacy").tasks.named("jar"),
    )
    archiveBaseName.set("AutoPlanting")
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("")

    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "com.autoplanting.AutoPlantingPlugin"
    }
}
