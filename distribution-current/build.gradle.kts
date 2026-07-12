plugins {
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":plugin-common"))
    implementation(project(":platform-bukkit"))
    implementation(project(":platform-paper"))
    implementation(project(":platform-folia"))
    implementation(project(":version-current"))
}

tasks.shadowJar {
    archiveClassifier.set("plain")
    archiveBaseName.set("AutoPlanting")
    archiveVersion.set(project.version.toString())
}
